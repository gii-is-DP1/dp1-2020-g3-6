package org.springframework.samples.foorder.web;

import java.security.Principal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.foorder.model.Comanda;
import org.springframework.samples.foorder.model.Plato;
import org.springframework.samples.foorder.model.PlatoPedido;
import org.springframework.samples.foorder.model.PlatoPedidoDTO;
import org.springframework.samples.foorder.service.ComandaService;
import org.springframework.samples.foorder.service.PlatoPedidoService;
import org.springframework.samples.foorder.service.PlatoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/comanda")
public class ComandaController {
	
	private ComandaService comandaService;
	private PlatoPedidoService platoPedidoService;
	private PlatoService platoService;
	
	@Autowired
	public ComandaController(ComandaService comandaService, PlatoPedidoService platoPedidoService, 
			PlatoService platoService) {
		super();
		this.comandaService = comandaService;
		this.platoPedidoService = platoPedidoService;
		this.platoService = platoService;
	}
	
	//Vista de Propietario para la lista total de Comandas
	@GetMapping(path="/listaComandaTotal")
	public String listadoComandaTotal(ModelMap modelMap) {
		String vista= "comanda/listaComandaTotal";
		Iterable<Comanda> comanda = comandaService.findAll();
		modelMap.addAttribute("comanda",comanda);
		return vista;	
	}
	
	//Vista de Propietario para la lista de Comandas de UN DIA concreto
	@GetMapping(path="/listaComandaTotal/dia")
	public String listadoComandaDia(String date, ModelMap modelMap) {
		String vista= "comanda/listaComandaTotal";
		if (date=="") {
			vista="redirect:/comanda/listaComandaTotal?message=Debes elegir un dia obligatoriamente";
			return vista;
		}
		Collection<Comanda> comanda = comandaService.encontrarComandaDia(date);
		modelMap.addAttribute("comanda",comanda);
		return vista;	
	}
	
	//Vista de Camarero para la lista actual de Comandas sin finalizar
	@GetMapping(path="/listaComandaActual")
	public String listadoComandaActual(ModelMap modelMap) {
		String vista= "comanda/listaComandaActual";
		Collection<Comanda> comanda = comandaService.encontrarComandaActual();
		modelMap.addAttribute("new_comanda", new Comanda());
		modelMap.addAttribute("list_comanda",comanda);
		return vista;	
	}
	
	//Vista de Camarero para la lista actual de Comandas sin finalizar
	@GetMapping(path="/listaComandaActual/finalizarComanda/{comandaID}")
	public String finalizarComanda(@PathVariable("comandaID") int comandaID, ModelMap modelMap) {
		String vista= "comanda/listaComandaActual";
		Optional<Comanda> comanda = comandaService.findById(comandaID);
		String message="";
		if(comanda.isPresent()) {
			Comanda res = comanda.get();
			if(comandaService.estaFinalizado(res)) {
				message="Esta comanda aún tiene platos por finalizar";
			}
			else if(res.getFechaFinalizado() == null) {
				res.setFechaFinalizado(LocalDateTime.now());
				message="La comanda se ha finalizado correctamente";
				comandaService.save(res);
			}else {
				message="La comanda ya está finalizada";
			}
			vista="redirect:/comanda/listaComandaActual?message="+message;
		}else {
			vista = "redirect:/comanda/listaComandaActual?message=La comanda pedida no existe";
		}
		return vista;
	}
	
	//Vista de Camarero para la lista de platos de una comanda
	@GetMapping(path="/listaComandaActual/{comandaID}")
	public String infoComanda(@PathVariable("comandaID") int comandaID, ModelMap modelMap) {
		String vista= "comanda/comandaDetails";		
		Comanda comanda = comandaService.findById(comandaID).get();
		Iterable<Plato> listaPlatos = platoService.findAllAvailable();
		List<PlatoPedido> platosEC= (List<PlatoPedido>) comandaService.getPlatosPedidoDeComanda(comandaID);
		
		modelMap.addAttribute("platopedido",new PlatoPedidoDTO());
		modelMap.addAttribute("platop",platosEC);
		modelMap.addAttribute("comanda",comanda);
		modelMap.addAttribute("listaPlatos",listaPlatos);
		return vista;
	}
	
	@PostMapping(path="/listaComandaActual/new")
	public String crearComanda(@Valid Comanda comanda, BindingResult result,ModelMap modelMap,Principal user) {
		if(result.hasErrors()) {
			modelMap.addAttribute("new_comanda", comanda);
			modelMap.addAttribute("message",result.getAllErrors().get(0).getDefaultMessage());
			Collection<Comanda> list_comanda = comandaService.encontrarComandaActual();
			modelMap.addAttribute("list_comanda",list_comanda);
			return "comanda/listaComandaActual";
		}
		Comanda new_comanda = comandaService.instanciarComanda(comanda, user);
		comandaService.save(new_comanda);
        int comandaId = comandaService.findLastId();
        modelMap.addAttribute("comanda", new_comanda);
        modelMap.addAttribute("message", "Guardado Correctamente");
        return "redirect:/comanda/listaComandaActual/"+comandaId;
    }
	
	
	@PostMapping(path="/listaComandaActual/asignar/{comandaId}/{ppId}")
	public String asignarComanda(@PathVariable("comandaId") Integer comandaId, @PathVariable("ppId") Integer ppId, ModelMap modelMap) throws ParseException {
		String vista= "";
		PlatoPedido plato = platoPedidoService.findById(ppId).get();
		if(plato.getIngredientesPedidos().size()==0){
			modelMap.addAttribute("message", "Ha habido un error al guardar, No se han seleccionado ingredientes");
			vista = "redirect:/platopedido/comanda/"+comandaId+"/"+ppId;
		}else {
			comandaService.anadirComandaAPlato(plato, comandaId);
			vista= "redirect:/comanda/listaComandaActual/"+comandaId;
		}
		return vista; 
	}
}