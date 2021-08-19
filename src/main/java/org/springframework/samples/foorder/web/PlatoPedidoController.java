package org.springframework.samples.foorder.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.foorder.model.EstadoPlato;
import org.springframework.samples.foorder.model.IngredientePedido;
import org.springframework.samples.foorder.model.PlatoPedido;
import org.springframework.samples.foorder.model.PlatoPedidoDTO;
import org.springframework.samples.foorder.service.EstadoPlatoService;
import org.springframework.samples.foorder.service.IngredientePedidoService;
import org.springframework.samples.foorder.service.PlatoPedidoService;
import org.springframework.samples.foorder.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping(value = "/platopedido")
public class PlatoPedidoController {
	
	private PlatoPedidoService platoPedidoService;
	private IngredientePedidoService ingredientePedidoService;
	private EstadoPlatoService estadoPlatoService;
	private PlatoPedidoConverter ppConverter;
	private EstadoPlatoFormatter estadoPlatoFormatter;
	private PlatoFormatter platoFormatter;
	private UserService userService;
	
	@Autowired
	public PlatoPedidoController(PlatoPedidoService platoPedidoService,
			IngredientePedidoService ingredientePedidoService,
			EstadoPlatoService estadoPlatoService, PlatoPedidoConverter ppConverter,
			EstadoPlatoFormatter estadoPlatoFormatter, PlatoFormatter platoFormatter, UserService userService) {
		super();
		this.platoPedidoService = platoPedidoService;
		this.ingredientePedidoService = ingredientePedidoService;
		this.estadoPlatoService = estadoPlatoService;
		this.ppConverter = ppConverter;
		this.estadoPlatoFormatter = estadoPlatoFormatter;
		this.platoFormatter = platoFormatter;
		this.userService=userService;
	}

	@ModelAttribute("estadoplatopedido") // Esto pertenece a EstadoPlato
	public Collection<EstadoPlato> poblarEstadosPlato() {
		return estadoPlatoService.findAll();
		
	}

	@GetMapping()
	public String listadoPlatosPedido(ModelMap modelMap) {
		String authority = this.userService.findAuthoritiesByUsername(this.userService.getUserSession().getUsername());
		if(!authority.equals("cocinero")) {
			return "errors/error-403";
		}
		String vista = "platosPedido/listaPlatosPedido";
		Iterable<PlatoPedido> pp = platoPedidoService.platosPedidosDesponibles();
		modelMap.addAttribute("platopedido", pp);
		return vista;
	}

	@PostMapping(path = "{comandaId}/save")
	public String guardarPP(@PathVariable("comandaId") int comandaId, @Valid PlatoPedidoDTO ppDTO,
			BindingResult result, ModelMap modelMap) throws ParseException {	
		if(ppDTO.getPlatodto() == null) {
			return "redirect:/comanda/listaComandaActual/"+comandaId;
		}
		String vista = "platosPedido/listaPlatosPedido";
		final PlatoPedido ppFinal = ppConverter.convertPPDTOToEntity(ppDTO);
		ppFinal.setEstadoplato(estadoPlatoFormatter.parse(ppDTO.getEstadoplatodto(), Locale.ENGLISH));
		ppFinal.setPlato(platoFormatter.parse(ppDTO.getPlatodto(), Locale.ENGLISH));
		if (result.hasErrors()) {
			vista="redirect:/platopedido/comanda/"+comandaId+"/"+ppFinal.getId()
			+"?message=ha habido un error al guardar"+ result.getAllErrors().toString();
		} else {
			platoPedidoService.save(ppFinal);
			vista="redirect:/platopedido/comanda/"+comandaId+"/"+ppFinal.getId()+"?message=guardado correctamente&comandaId="+comandaId;
		}
		return vista;
	}

	@GetMapping(value = "/comanda/{comandaId}/{ppId}")
	public String initUpdatePPForm(@PathVariable("comandaId") int comandaId, @PathVariable("ppId") int ppId,
			ModelMap model) {
		String vista = "platosPedido/modificarIngredientes";
		Collection<EstadoPlato> collectionEstadosPlato = estadoPlatoService.findAll();

		Collection<String> listaPlatos = new ArrayList<String>();

		PlatoPedido pp = platoPedidoService.findById(ppId).get();
		listaPlatos.add(pp.getPlato().getName());

		// Asignación de ingredientespedidos a plato pedido
		Collection<IngredientePedido> ingredientes = ingredientePedidoService.CrearIngredientesPedidos(pp);

		model.addAttribute("comandaId", comandaId);
		model.addAttribute("estadosPlato", collectionEstadosPlato);
		model.addAttribute("listaPlatos", listaPlatos);
		model.addAttribute("platopedidoId", ppId);
		model.addAttribute("ingredientespedido", ingredientes);
		return vista;
	}

	@PostMapping(value = "{comandaId}/guardarIngrediente/{ppId}/{ingId}")
	public String guardarIngrediente(@PathVariable("comandaId") int comandaId, @PathVariable("ppId") int ppId,
			@PathVariable("ingId") int ingId, @Valid IngredientePedido ingredientePedido, BindingResult result,
			ModelMap modelMap) throws ParseException {
			ingredientePedidoService.save(ingredientePedido, ingId, ppId);
			log.info(String.format("IngredientOrder with ingredient %s and amount %f has been saved", ingredientePedido.getIngrediente().getProducto().getName(), ingredientePedido.getCantidadPedida()));
			return "redirect:/platopedido/comanda/"+comandaId+"/"+ppId+"?message=agregado";
		
	}

	// parte correspondiente a ingrediente pedido
	@GetMapping("/{ppId}")
	public String showIngredientePedido(@PathVariable("ppId") int ppId, ModelMap model) {
		PlatoPedido pp = platoPedidoService.findById(ppId).get();
		model.addAttribute("platopedido", pp);
		List<IngredientePedido> ls = ingredientePedidoService.findByPlatoPedidoId(ppId);
		model.addAttribute("ingredientespedido", ls);
		return "platosPedido/ingredientesDePlatoPedido";

	}

	// Modifica el estado del plato al siguiente
	@GetMapping(path = "/modificarEstado/{platopedidoID}/{cambiarA}")
	public String Stock(@PathVariable("platopedidoID") Integer ppId, @PathVariable("cambiarA") String estado,
			ModelMap model) throws ParseException {
		String authority = this.userService.findAuthoritiesByUsername(this.userService.getUserSession().getUsername());
		if(!authority.equals("cocinero")) {
			return "errors/error-403";
		}
		String vista="";
		Optional<PlatoPedido> pp = platoPedidoService.findById(ppId);
		if (pp.isPresent()) {
			PlatoPedido p = pp.get();
			p.setEstadoplato(estadoPlatoFormatter.parse(estado, Locale.ENGLISH));
			this.platoPedidoService.save(p);
			vista="redirect:/platopedido?message=Se ha cambiado el plato con exito";
			return vista;
		} else {
			vista="redirect:/platopedido?message=No se ha cambiado el plato con exito";
			return vista;
		}
	}
}