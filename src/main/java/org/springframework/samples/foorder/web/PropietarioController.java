package org.springframework.samples.foorder.web;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.foorder.model.Propietario;
import org.springframework.samples.foorder.service.AuthoritiesService;
import org.springframework.samples.foorder.service.PropietarioService;
import org.springframework.samples.foorder.validators.PropietarioValidator;
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
@RequestMapping(value = "/propietarios")
public class PropietarioController {

	private PropietarioService propietarioService;
	private AuthoritiesService authoritiesService;
	
	@Autowired
	public PropietarioController(PropietarioService propietarioService, AuthoritiesService authoritiesService) {
		super();
		this.propietarioService = propietarioService;
		this.authoritiesService = authoritiesService;
	}

	@InitBinder("propietario")
	public void initPropietarioBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new PropietarioValidator(this.authoritiesService));
	}
	
	@GetMapping()
	public String listadoPropietarios(ModelMap modelMap) {
		String vista = "propietarios/listaPropietarios";
		if(propietarioService.count()==0) {
			modelMap.addAttribute("message", "la lista esta vacia");
			return vista;
		}else {
			Iterable<Propietario> propietarios = propietarioService.findAll();
			modelMap.addAttribute("propietarios", propietarios);
			return vista;
		}
	}

	@GetMapping(path = "/new")
	public String crearPropietario(ModelMap modelMap) {
		String vista = "propietarios/editPropietario";
		modelMap.addAttribute("propietario", new Propietario());
		return vista;
	}

	@PostMapping(path = "/save")
	public String guardarPropietario(@Valid Propietario propietario, BindingResult result, ModelMap modelMap) {
		String vista = "propietarios/listaPropietarios";
		if (result.hasErrors()) {
			log.info(String.format("Owner with name %s wasn't able to be created", propietario.getName()));
			modelMap.addAttribute("propietario", propietario);
			return "propietarios/editPropietario";
		} else {
			propietarioService.save(propietario);
			vista = "redirect:/propietarios?message=Guardado correctamente";
		}
		return vista;
	}

	@GetMapping(path = "/delete/{propietarioId}")
	public String borrarPropietario(@PathVariable("propietarioId") final int propietarioId, final ModelMap modelMap) {
		Optional<Propietario> propietario = this.propietarioService.findById(propietarioId);
		String vista="";
		if (propietario.isPresent()) {
			propietarioService.deleteById(propietario.get().getId());
			vista="redirect:/propietarios?message=Borrado correctamente";
		} else {
			vista="redirect:/propietarios?message=Propietario no encontrado";
		}
		return vista;
	}

	@GetMapping(value = "/edit/{propietarioId}")
	public String initUpdatePropietarioForm(@PathVariable("propietarioId") int propietarioId, ModelMap model) {
		String vista = "propietarios/editarPropietario";
		Propietario propietario = propietarioService.findById(propietarioId).get();
		model.addAttribute(propietario);
		return vista;
	}

	@PostMapping(value = "/edit")
	public String processUpdatePropietarioForm(@Valid Propietario propietario, BindingResult result, ModelMap modelMap) {
		if(this.propietarioService.propietarioConMismoUsuario(propietario)) {
			result = this.propietarioService.erroresSinMismoUser(propietario, result);
		}
		if (result.hasErrors()) {
			modelMap.addAttribute("propietario", propietario);
			return "propietarios/editarPropietario";
		} else {
			this.propietarioService.save(propietario);
			return "redirect:/propietarios?message=Actualizado correctamente";
		}
	}
}
