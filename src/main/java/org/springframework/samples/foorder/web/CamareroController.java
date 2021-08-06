package org.springframework.samples.foorder.web;

import java.util.Iterator;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.foorder.model.Camarero;
import org.springframework.samples.foorder.service.AuthoritiesService;
import org.springframework.samples.foorder.service.CamareroService;
import org.springframework.samples.foorder.validators.CamareroValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping(value = "/camareros")
public class CamareroController {
	
	private CamareroService camareroService;
	private AuthoritiesService authoritiesService;
	
	@Autowired
	public CamareroController(CamareroService camareroService, AuthoritiesService authoritiesService) {
		super();
		this.camareroService = camareroService;
		this.authoritiesService = authoritiesService;
	}
	
	@InitBinder("camarero")
	public void initCamareroBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new CamareroValidator());
	}

	@GetMapping()
	public String listadoCamareros(ModelMap modelMap) {
		String vista= "camareros/listaCamareros";
		Iterable<Camarero> camareros=  camareroService.findAll();
		Iterator<Camarero> it_camareros = camareros.iterator();
		
		if (!(it_camareros.hasNext())) {
			modelMap.addAttribute("message", "No hay camareros, contrata a alguien y crea su Ficha de Empleado");
		}
		
		modelMap.addAttribute("camareros",camareros);
		return vista;
		
	}
	
	@GetMapping(path="/new")
	public String crearCamarero(ModelMap modelMap) {
		String vista= "camareros/editCamarero";
		modelMap.addAttribute("camarero",new Camarero());
		modelMap.addAttribute("usernames", authoritiesService.findAllUsernames());
		return vista;
	}
	
	@PostMapping(path="/save")
	public String save(@Valid Camarero camarero,BindingResult result,ModelMap modelMap) {
		String vista= "camareros/listaCamareros";
		FieldError error=this.camareroService.resultUserSave(camarero, result);
		if(error!=null) {
			result.addError(error);
		}
		if(result.hasErrors()) {
			log.info(String.format("Waiter with name %s wasn't able to be created", camarero.getName(), camarero.getId()));
			modelMap.addAttribute("camarero", camarero);
			return "camareros/editCamarero";
		}else {
			camareroService.save(camarero);
			vista="redirect:/camareros?message=Guardado correctamente";
		}
		return vista;
	}
	
	@GetMapping(path="/delete/{camareroId}")
	public String borrarCamarero(@PathVariable("camareroId") int camareroId, ModelMap modelMap) {
		String vista= "camareros/listaCamareros";
		Optional<Camarero> cam= camareroService.findById(camareroId);
		if(cam.isPresent()) {
			camareroService.deleteById(camareroId);
			vista="redirect:/camareros?message=Borrado correctamente";
		}else {
			vista="redirect:/camareros?message=Camarero no encontrado";
		}
		return vista;
	}
	
	@GetMapping(value = "/edit/{camareroId}")
	public String initUpdateCamareroForm(@PathVariable("camareroId") int camareroId, ModelMap model) {
		Optional<Camarero> cam=camareroService.findById(camareroId);
		if(cam.isEmpty()) {
			return "redirect:/camareros?message=Ese camarero no existe";
		}
		String vista= "camareros/editarCamareros";
		Camarero cam1 =  cam.get();
		model.addAttribute(cam1);
		return vista;
	}
	
	@PostMapping(value = "/edit")
	public String processUpdateCamareroForm(@Valid Camarero camarero, BindingResult result,ModelMap modelMap) {
		FieldError error=this.camareroService.resultUserEdit(camarero, result);
		if(error!=null) {
			result.addError(error);
		}
		if(result.hasErrors()) {
			System.out.println(result.getAllErrors());
			log.info(String.format("Waiter with name %s and ID %d wasn't able to be updated", camarero.getName(), camarero.getId()));
			modelMap.addAttribute("camarero", camarero);
			return "camareros/editarCamareros";
		}else {
			camareroService.save(camarero);
			return "redirect:/camareros?message=Actualizado correctamente";
		}
	}
}
