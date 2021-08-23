package org.springframework.samples.foorder.web;

import org.springframework.beans.BeanUtils;
import org.springframework.samples.foorder.model.PlatoPedido;
import org.springframework.samples.foorder.model.PlatoPedidoDTO;
import org.springframework.stereotype.Component;

@Component
public class PlatoPedidoConverter {
	
	public PlatoPedido convertPPDTOToEntity(PlatoPedidoDTO pp) {
		PlatoPedido res = new PlatoPedido();
		BeanUtils.copyProperties(pp, res);
		return res;	
	}
	
	public PlatoPedidoDTO convertEntityToPPDTO(PlatoPedido pp) {
		PlatoPedidoDTO res = new PlatoPedidoDTO();
		BeanUtils.copyProperties(pp, res);
		return res;	
	}
}
