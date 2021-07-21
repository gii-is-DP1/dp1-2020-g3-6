package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Comanda;
import org.springframework.samples.petclinic.model.Plato;
import org.springframework.samples.petclinic.model.PlatoPedido;
import org.springframework.samples.petclinic.model.PlatoPedidoDTO;
import org.springframework.samples.petclinic.service.ComandaService;
import org.springframework.samples.petclinic.service.PlatoPedidoService;
import org.springframework.samples.petclinic.service.PlatoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
			modelMap.addAttribute("message", "Debes elegir un dia obligatoriamente");
			vista = listadoComandaTotal(modelMap);
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
		modelMap.addAttribute("comanda",comanda);
		return vista;	
	}
	
	//Vista de Camarero para la lista actual de Comandas sin finalizar
	@GetMapping(path="/listaComandaActual/finalizarComanda/{comandaID}")
	public String finalizarComanda(@PathVariable("comandaID") int comandaID, ModelMap modelMap) {
		String vista= "comanda/listaComandaActual";
		Optional<Comanda> comanda = comandaService.findById(comandaID);
		if(comanda.isPresent()) {
			Comanda res = comanda.get();
			if(comandaService.estaFinalizado(res)) {
					modelMap.addAttribute("message", "Esta comanda aún tiene platos por finalizar");
					vista = listadoComandaActual(modelMap);
					return vista;
			}
			if(res.getFechaFinalizado()==null) {
				res.setFechaFinalizado(LocalDateTime.now());
				modelMap.addAttribute("message", "La comanda se ha finalizado correctamente");
				vista = listadoComandaActual(modelMap);
			}else {
				modelMap.addAttribute("message", "La comanda ya está finalizada");
				vista = listadoComandaActual(modelMap);
			}
		}else {
			modelMap.addAttribute("message", "La comanda pedida no existe");
			vista = "redirect:/comanda/listaComandaActual";
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
	
	@GetMapping(path="/listaComandaActual/new")
	public String crearComanda(Integer mesa,ModelMap modelMap,Principal user) {
        Comanda comanda = comandaService.crearComanda(mesa, user);
        int comandaId = comandaService.findLastId();
        modelMap.addAttribute("comanda", comanda);
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