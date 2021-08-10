package org.springframework.samples.foorder.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.foorder.model.Cocinero;
import org.springframework.samples.foorder.model.User;
import org.springframework.samples.foorder.repository.CocineroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CocineroService {
	
	private UserService userService;
	private AuthoritiesService authoritiesService;
	private CocineroRepository cocineroRepository;
	
	@Autowired
	public CocineroService(UserService userService, AuthoritiesService authoritiesService,
			CocineroRepository cocineroRepository) {
		super();
		this.userService = userService;
		this.authoritiesService = authoritiesService;
		this.cocineroRepository = cocineroRepository;
	}

	public Iterable<Cocinero> findAll() {
		return cocineroRepository.findAll();
	}
	
	public Optional<Cocinero> findById(Integer id) {
		return cocineroRepository.findById(id);
	}

	@Transactional
	public Cocinero save(Cocinero cocinero) {
		User user = authoritiesService.crearUsuario(cocinero.getUsuario(), cocinero.getContrasena());
		if(cocinero.getId()!=null) {	
			String antiguo = this.cocineroRepository.findById(cocinero.getId()).get().getUsuario();
			if(!user.getUsername().equals(antiguo)) {
				userService.deleteUser(userService.findUser(antiguo).get());
			}
		}
		userService.saveUser(user);
		authoritiesService.saveAuthorities(cocinero.getUsuario(), "cocinero");
		log.info(String.format("Chef with username %s has been saved", cocinero.getUsuario(),
				cocinero.getId()));
		return cocineroRepository.save(cocinero);
	}

	@Transactional
	public void deleteById(Integer id) {
		Cocinero cocinero = cocineroRepository.findById(id).get();
		this.userService.deleteUser(this.userService.findUser(cocinero.getUsuario()).get());
		cocineroRepository.deleteById(id);
		log.info(String.format("Chef with username %s has been deleted", cocinero.getUsuario()));
	}
	
	@Transactional(readOnly = true)
	public Boolean cocineroConMismoUsuario(Cocinero cocinero ) throws DataAccessException {
		Boolean res=false;
		Integer cocineroId= cocinero.getId();
		Cocinero comp=this.cocineroRepository.findById(cocineroId).get();
		if(comp.getUsuario().equals(cocinero.getUsuario())) {
			res=true;
		}
		return res;
	}
	
	@Transactional(readOnly = true)
	public FieldError resultUserSave(Cocinero cocinero, BindingResult result) throws DataAccessException {
		if(authoritiesService.findAllUsernames().contains(cocinero.getUsuario())) {
			FieldError error= new FieldError("cocinero", "usuario", cocinero.getUsuario(), false, null, null, "este usuario esta repetido");
			return error;
		}
		return null;
	}
	
	@Transactional(readOnly = true)
	public FieldError resultUserEdit(Cocinero cocinero, BindingResult result) throws DataAccessException {
		if(authoritiesService.findAllUsernames().contains(cocinero.getUsuario())&&!this.cocineroConMismoUsuario(cocinero)) {
			FieldError error= new FieldError("camarero", "usuario", cocinero.getUsuario(), false, null, null, "este usuario esta repetido");
			return error;
		}
		return null;
	}
}
