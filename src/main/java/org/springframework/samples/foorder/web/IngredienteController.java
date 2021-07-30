package org.springframework.samples.foorder.web;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.foorder.model.Ingrediente;
import org.springframework.samples.foorder.service.IngredienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/ingrediente")
public class IngredienteController {
	
	private IngredienteService ingredienteService;
	
	@Autowired
	public IngredienteController(IngredienteService ingredienteService) {
		super();
		this.ingredienteService = ingredienteService;
	}

	@GetMapping()
	public String listadoIngrediente(ModelMap modelMap) {
		String vista= "ingrediente/listaIngredientes";
		Iterable<Ingrediente> inglist = ingredienteService.findAll();
		modelMap.addAttribute("ingrediente",inglist);
		return vista;	
	}

	@GetMapping(path="/delete/{ingId}")
	public String borrarIngrediente(@PathVariable("ingId") int ingId, ModelMap modelMap) {
		String vista= "ingrediente/listaIngredientes";
		Optional<Ingrediente> pp= ingredienteService.findById(ingId);
		if(pp.isPresent()) {
			ingredienteService.deleteById(ingId);
			vista="redirect:/ingrediente?message=Borrado Correctamente";
		}else {
			vista="redirect:/ingrediente?message=Ingrediente no encontrado";
		}
		return vista;
	}
}
