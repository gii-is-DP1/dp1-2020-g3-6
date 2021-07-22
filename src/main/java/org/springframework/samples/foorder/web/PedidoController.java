package org.springframework.samples.foorder.web;

import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.foorder.model.Pedido;
import org.springframework.samples.foorder.service.PedidoService;
import org.springframework.samples.foorder.service.exceptions.DuplicatedPedidoException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/pedidos")
public class PedidoController {
	
	private PedidoService pedidoService;
	
	@Autowired
	public PedidoController(PedidoService pedidoService) {
		super();
		this.pedidoService = pedidoService;
	}

	@GetMapping()
	public String listadoDePedidos(ModelMap modelMap) {
		String vista="pedidos/listaPedidos";
		Iterable<Pedido> pedido= pedidoService.findAll();
		modelMap.addAttribute("pedido", pedido);
		return vista;
	}
	
	@GetMapping(path="/porProveedor")
	public String listadoDePedidosPorProveedor(Integer proveedorID, ModelMap modelMap) {
		String vista="pedidos/listaPedidos";
		Iterable<Pedido> pedido= pedidoService.findByProveedorId(proveedorID);
		modelMap.addAttribute("pedido", pedido);
		return vista;
	}
	
	
	@GetMapping(path="/new")
	public String crearPedido(ModelMap modelMap) {
		String view= "pedidos/editPedido";
		modelMap.addAttribute("pedido",new Pedido());
		return view;
	}
	
	@PostMapping(path="/save")
	public String guardarPedido(@Valid Pedido pedido,BindingResult result,ModelMap modelMap) {
		String view= "pedidos/listaPedidos";
		
		if(result.hasErrors()) {
			log.info(String.format("Order wasn't able to be created"));
			modelMap.addAttribute("pedido", pedido);
			return "pedidos/editPedido";
		}else {
			 try {                    
				pedidoService.save(pedido);
				modelMap.addAttribute("message", "Guardado Correctamente");
				view=listadoDePedidos(modelMap);              
             } catch (DuplicatedPedidoException ex) {
                 result.rejectValue("proveedor", "duplicate", "already exists");
                 view= "pedidos/listaPedidos";
             }
		}
		return view	;
	}
	

	@GetMapping(path="/terminarPedido/{pedidoID}")
	public String recargarStock(@PathVariable("pedidoID") int pedidoID, ModelMap modelMap) throws DataAccessException, DuplicatedPedidoException {
		String view= "pedidos/listaPedidos";
		Optional<Pedido> pedi = pedidoService.findById(pedidoID);
		if(pedi.isPresent()) {
			Pedido p = pedi.get();
			if (p.getHaLlegado().equals(Boolean.FALSE)) {
				pedidoService.recargarStock(pedidoID);
				modelMap.addAttribute("message", "Se ha finalizado el pedido correctamente");
				modelMap.addAttribute("pedidoFinalizado", p);
				view = listadoDePedidos(modelMap);
			} else {
				modelMap.addAttribute("message", "El pedido ya se ha finalizado");
				view = listadoDePedidos(modelMap);
			}
		}else {
			modelMap.addAttribute("message", "not found");
			view = listadoDePedidos(modelMap);
		}
		return view;
	}
	
	//Todas las comandas de x dia. 
		@GetMapping(path="/listaPedidoTotal/dia")
		public String listadoPedidoDia(String date, ModelMap modelMap) {
			String vista= "pedidos/listaPedidos";
			if (date=="") {
				modelMap.addAttribute("message", "Debes elegir un dia obligatoriamente");
				vista = listadoDePedidos(modelMap);
				return vista;
			}
			Collection<Pedido> pedido = pedidoService.encontrarPedidoDia(date);
			modelMap.addAttribute("pedido",pedido);
			return vista;	
		}
}
