package org.springframework.samples.foorder.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.foorder.model.Manager;
import org.springframework.samples.foorder.model.User;
import org.springframework.samples.foorder.repository.ManagerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ManagerService {
	
	private AuthoritiesService authoritiesService;
	private UserService userService;
	private ManagerRepository managerRepository;
	
	@Autowired
	public ManagerService(AuthoritiesService authoritiesService, UserService userService,
			ManagerRepository managerRepository) {
		super();
		this.authoritiesService = authoritiesService;
		this.userService = userService;
		this.managerRepository = managerRepository;
	}

	public Iterable<Manager> findAll() {
		return managerRepository.findAll();
	}
	
	public Optional<Manager> findById(Integer id) {
		return managerRepository.findById(id);
	}

	@Transactional
	public void save(Manager manager) {
		User user = authoritiesService.crearUsuario(manager.getUsuario(), manager.getContrasena());
		if(manager.getId()!=null) {	
			String antiguo = this.managerRepository.findById(manager.getId()).get().getUsuario();
			if(!user.getUsername().equals(antiguo)) {
				userService.deleteUser(userService.findUser(antiguo).get());
			}
		}	
		userService.saveUser(user);
		authoritiesService.saveAuthorities(manager.getUsuario(), "manager");
		managerRepository.save(manager);
		log.info(String.format("Manager with username %s been save correctly", manager.getUsuario(),
				manager.getId()));
		
	}

	@Transactional
	public void deleteById(Integer id) {
		Manager manager = managerRepository.findById(id).get();
		this.userService.deleteUser(this.userService.findUser(manager.getUsuario()).get());
		managerRepository.deleteById(id);
		log.info(String.format("Manager with username %s has been deleted", manager.getUsuario()));
	}
	
	@Transactional(readOnly = true)
	public Boolean managerConMismoUsuario(Manager manager) throws DataAccessException {
		Boolean res=false;
		Integer managerId= manager.getId();
		Manager comp=this.managerRepository.findById(managerId).get();
		if(comp.getUsuario().equals(manager.getUsuario())) {
			res=true;
		}
		return res;
	}
	
	@Transactional(readOnly = true)
	public FieldError resultUserSave(Manager manager, BindingResult result) throws DataAccessException {
		if(authoritiesService.findAllUsernames().contains(manager.getUsuario())) {
			FieldError error= new FieldError("manager", "usuario", manager.getUsuario(), false, null, null, "este usuario esta repetido");
			return error;
		}
		return null;
	}
	
	@Transactional(readOnly = true)
	public FieldError resultUserEdit(Manager manager, BindingResult result) throws DataAccessException {
		if(authoritiesService.findAllUsernames().contains(manager.getUsuario())&&!this.managerConMismoUsuario(manager)) {
			FieldError error= new FieldError("manager", "usuario", manager.getUsuario(), false, null, null, "este usuario esta repetido");
			return error;
		}else {
			return null;
		}
	}
}
