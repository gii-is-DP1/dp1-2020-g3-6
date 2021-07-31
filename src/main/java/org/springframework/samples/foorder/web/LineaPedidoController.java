package org.springframework.samples.foorder.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.foorder.model.LineaPedido;
import org.springframework.samples.foorder.service.LineaPedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

	@GetMapping()
	public String listadoDeLineaPedido(ModelMap modelMap) {
		String vista="lineaPedido/listaLineaPedido";
		Iterable<LineaPedido> lineaPedido= lineaPedidoService.findAll();
		modelMap.addAttribute("lineaPedido", lineaPedido);
		return vista;
	}
	
	@GetMapping(path="/porPedido")
	public String listadoDeLineaPedidosPorPedido(Integer pedidoID, ModelMap modelMap) {
		String vista="lineaPedido/listaLineaPedido";
		Iterable<LineaPedido> lineapedido= lineaPedidoService.findByPedidoId(pedidoID);
		modelMap.addAttribute("lineaPedido", lineapedido);
		return vista;
	}
	
	@PostMapping(path="/save")
	public String guardarLineaPedido(@Valid LineaPedido lineaPedido,BindingResult result,ModelMap modelMap) {
		String vista= "lineaPedido/listaLineaPedido";
		
		if(result.hasErrors()) {
			modelMap.addAttribute("lineaPedido", lineaPedido);
			return "lineaPedido/editLineaPedido";
		}else {
			lineaPedidoService.save(lineaPedido);
			vista="redirect:/lineaPedido?message=Guardado Correctamente";
		}
		return vista	;
	}
}

