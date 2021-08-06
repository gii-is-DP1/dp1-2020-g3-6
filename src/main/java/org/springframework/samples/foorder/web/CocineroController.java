package org.springframework.samples.foorder.web;

import java.util.Iterator;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.foorder.model.Cocinero;
import org.springframework.samples.foorder.service.CocineroService;
import org.springframework.samples.foorder.validators.CocineroValidator;
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
@RequestMapping(value = "/cocinero")
public class CocineroController {
	
	private CocineroService cocineroService;
	
	@Autowired
	public CocineroController(CocineroService cocineroService) {
		super();
		this.cocineroService = cocineroService;
	}
	
	@InitBinder("cocinero")
	public void initCocineroBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new CocineroValidator());
	}
	
	@GetMapping()
	public String listadoCocinero(ModelMap modelMap) {
		String vista = "cocinero/listaCocinero";
		Iterable<Cocinero> cocinero = cocineroService.findAll();
		Iterator<Cocinero> it_cocinero = cocinero.iterator();

		if (!(it_cocinero.hasNext())) {
			modelMap.addAttribute("message", "No hay cocineros, contrata a alguien y crea su Ficha de Empleado");
		}
		modelMap.addAttribute("cocinero", cocinero);

		return vista;
	}

	@GetMapping(path = "/new")
	public String crearCocinero(ModelMap modelMap) {
		String vista = "cocinero/editCocinero";
		modelMap.addAttribute("cocinero", new Cocinero());
		return vista;
	}

	@PostMapping(path = "/save")
	public String save(@Valid Cocinero cocinero, BindingResult result, ModelMap modelMap) {
		FieldError error=this.cocineroService.resultUserSave(cocinero, result);
		if(error!=null) {
			result.addError(error);
		}
		String vista = "cocinero/listaCocinero";
		if (result.hasErrors()) {
			log.info(String.format("Chef with name %s wasn't able to be created", cocinero.getName()));
			modelMap.addAttribute("cocinero", cocinero);
			return "cocinero/editCocinero";
		} else {
			cocineroService.save(cocinero);
			vista = "redirect:/cocinero?message=Guardado Correctamente";
		}
		return vista;
	}

	@GetMapping(path = "/delete/{cocineroId}")
	public String deleteById(@PathVariable("cocineroId") int cocineroId, ModelMap modelMap) {
		String vista = "cocinero/listaCocinero";
		Optional<Cocinero> cam = cocineroService.findById(cocineroId);
		if (cam.isPresent()) {
			cocineroService.deleteById(cocineroId);
			vista = "redirect:/cocinero?message=Borrado Correctamente";
		} else {
			vista = "redirect:/cocinero?message=Cocinero no encontrado";;
		}
		return vista;
	}

	@GetMapping(value = "/edit/{cocineroId}")
	public String initUpdateCocineroForm(@PathVariable("cocineroId") int cocineroId, ModelMap model) {
		Optional<Cocinero> coc=cocineroService.findById(cocineroId);
		if(coc.isEmpty()) {
			return "redirect:/cocinero?message=Ese cocinero no existe";
		}
		String vista = "cocinero/editarCocinero";
		Cocinero cocinero = cocineroService.findById(cocineroId).get();
		model.addAttribute(cocinero);
		return vista;
	}

	@PostMapping(value = "/edit")
	public String processUpdateCocineroForm(@Valid Cocinero cocinero, BindingResult result, ModelMap modelMap) {
		FieldError error=this.cocineroService.resultUserEdit(cocinero, result);
		if(error!=null) {
			result.addError(error);
		}
		if (result.hasErrors()) {
			log.info(String.format("Chef with name %s and ID %d wasn't able to be updated", cocinero.getName(), cocinero.getId()));
			modelMap.addAttribute("cocinero", cocinero);
			return "cocinero/editarCocinero";
		}else {
			this.cocineroService.save(cocinero);
			return "redirect:/cocinero?message=Cocinero actualizado";
		}
	}
}
