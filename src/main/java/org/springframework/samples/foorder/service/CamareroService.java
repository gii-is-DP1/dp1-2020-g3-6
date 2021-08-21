package org.springframework.samples.foorder.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.foorder.model.Camarero;
import org.springframework.samples.foorder.model.User;
import org.springframework.samples.foorder.repository.CamareroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CamareroService {
	
	private UserService userService;
	private CamareroRepository camareroRepository;
	private AuthoritiesService authoritiesService;
	
	@Autowired
	public CamareroService(UserService userService, CamareroRepository camareroRepository,
			AuthoritiesService authoritiesService) {
		super();
		this.userService = userService;
		this.camareroRepository = camareroRepository;
		this.authoritiesService = authoritiesService;
	}

	public Iterable<Camarero> findAll() {
		return camareroRepository.findAll();
	}
	
	public Optional<Camarero> findById(int camareroId) throws DataAccessException {
		return this.camareroRepository.findById(camareroId);
	}

	@Transactional
	public void save(Camarero camarero) {
		User user = authoritiesService.crearUsuario(camarero.getUsuario(), camarero.getContrasena());
		if(camarero.getId()!=null) {	
			String antiguo = this.camareroRepository.findById(camarero.getId()).get().getUsuario();
			if(!user.getUsername().equals(antiguo)) {
				userService.deleteUser(userService.findUser(antiguo).get());
			}
		}
		userService.saveUser(user);
		authoritiesService.saveAuthorities(camarero.getUsuario(), "camarero");
		camareroRepository.save(camarero);
		log.info(String.format("Waiter with username %s has been saved", camarero.getUsuario(),
				camarero.getId()));
	}

	@Transactional
	public void deleteById(Integer id) {
		Camarero camarero = camareroRepository.findById(id).get();
		this.userService.deleteUser(this.userService.findUser(camarero.getUsuario()).get());
		camareroRepository.deleteById(id);
		log.info(String.format("Waiter with username %s has been deleted", camarero.getUsuario()));
	}

    // Se usa para asignar un camarero a una comanda dado su usario
    @Transactional(readOnly = true)
    public Camarero findByUser(String user) {
        return this.camareroRepository.findByUsuario(user);
    }
	
	@Transactional(readOnly = true)
	public Boolean CamareroConMismoUsuario(Camarero camarero) throws DataAccessException {
		Boolean res=false;
		Integer camareroId= camarero.getId();
		Camarero comp=this.camareroRepository.findById(camareroId).get();
		if(comp.getUsuario().equals(camarero.getUsuario())) {
			res=true;
		}
		return res;
	}
	
	
	@Transactional(readOnly = true)
	public FieldError resultUserSave(Camarero camarero, BindingResult result) throws DataAccessException {
		if(authoritiesService.findAllUsernames().contains(camarero.getUsuario())) {
			FieldError error= new FieldError("camarero", "usuario", camarero.getUsuario(), false, null, null, "este usuario esta repetido");
			return error;
		}else {
			return null;
		}
	}
	
	@Transactional(readOnly = true)
	public FieldError resultUserEdit(Camarero camarero, BindingResult result) throws DataAccessException {
		if(authoritiesService.findAllUsernames().contains(camarero.getUsuario())&&!this.CamareroConMismoUsuario(camarero)) {
			FieldError error= new FieldError("camarero", "usuario", camarero.getUsuario(), false, null, null, "este usuario esta repetido");
			return error;
		}else {
			return null;
		}
	}
	
	
	
}