package org.springframework.samples.foorder.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.foorder.model.LineaPedido;
import org.springframework.samples.foorder.service.LineaPedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lineaPedido")
public class LineaPedidoController {
	
	private LineaPedidoService lineaPedidoService;
	
	@Autowired
	public LineaPedidoController(LineaPedidoService lineaPedidoService) {
		super();
		this.lineaPedidoService = lineaPedidoService;
	}
	
	@GetMapping(path="/porPedido")
	public String listadoDeLineaPedidosPorPedido(Integer pedidoID, ModelMap modelMap) {
		String vista="lineaPedido/listaLineaPedido";
		Iterable<LineaPedido> lineapedido= lineaPedidoService.findByPedidoId(pedidoID);
		modelMap.addAttribute("lineaPedido", lineapedido);
		return vista;
	}
	
}

