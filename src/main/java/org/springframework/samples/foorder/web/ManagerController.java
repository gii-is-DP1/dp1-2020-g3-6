package org.springframework.samples.foorder.web;

import java.util.Iterator;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.foorder.model.Manager;
import org.springframework.samples.foorder.service.AuthoritiesService;
import org.springframework.samples.foorder.service.ManagerService;
import org.springframework.samples.foorder.validators.ManagerValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/managers")
public class ManagerController {
	
	private ManagerService managerService;
	private AuthoritiesService authoritiesService;
	
	@Autowired
	public ManagerController(ManagerService managerService, AuthoritiesService authoritiesService) {
		super();
		this.managerService = managerService;
		this.authoritiesService = authoritiesService;
	}
	
	@InitBinder("manager")
	public void initManagerBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new ManagerValidator(this.authoritiesService));
	}

	@GetMapping()
	public String listadoManagers(ModelMap modelMap) {
		String vista = "managers/listaManagers";
		Iterable<Manager> managers = managerService.findAll();
		Iterator<Manager> it_managers = managers.iterator();
		
		if (!(it_managers.hasNext())) {
			modelMap.addAttribute("message", "No hay managers, contrata a alguien y crea su Ficha de Empleado");
		}
		
		modelMap.addAttribute("managers", managers);
		return vista;
	}
	
	@GetMapping(path="/new")
	public String crearManager(ModelMap modelMap) {
		String vista= "managers/editManager";
		modelMap.addAttribute("manager",new Manager());
		return vista;
	}
	
	@PostMapping(path="/save")
	public String save(@Valid Manager manager, BindingResult result, ModelMap modelMap) {
		String vista= "managers/listaManager";
		if(result.hasErrors()) {
			log.info(String.format("Manager with name %s wasn't able to be created", manager.getName()));
			modelMap.addAttribute("manager", manager);
			return "managers/editManager";
		}else {
			managerService.save(manager);
			vista="redirect:/managers?message=Guardado correctamente";
		}
		return vista;
	}
	
	@GetMapping(path="/delete/{managerId}")
	public String deleteById(@PathVariable("managerId") int managerId, ModelMap modelMap) {
		String vista= "managers/listaManagers";
		Optional<Manager> man= managerService.findById(managerId);
		if(man.isPresent()) {
			managerService.deleteById(managerId);
			vista="redirect:/managers?message=Borrado Correctamente";
		}else {
			vista="redirect:/managers?message=Manager no encontrado";
		}
		return vista;	
	}
	
	
	@GetMapping(value = "/edit/{managerId}")
	public String initUpdateManagerForm(@PathVariable("managerId") int managerId, ModelMap model) {
		String vista= "managers/editarManager";
		Manager manager =  managerService.findById(managerId).get();
		model.addAttribute(manager);
		return vista;
	}
	@PostMapping(value = "/edit")
	public String processUpdateManagerForm(@Valid Manager manager, BindingResult result,ModelMap modelMap) {
		if(this.managerService.managerConMismoUsuario(manager)) {
			result = this.managerService.erroresSinMismoUser(manager, result);
		}
		if(result.hasErrors()) {
			modelMap.addAttribute("manager", manager);
			log.info(String.format("Manager with name %s and ID %d wasn't able to be updated", manager.getName(), manager.getId()));
			return "managers/editarManager";
		}else {
			this.managerService.save(manager);
			return "redirect:/managers?message=Actualizado correctamente";
		}
	}
}