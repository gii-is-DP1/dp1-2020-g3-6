package org.springframework.samples.foorder.web;

import org.springframework.beans.BeanUtils;
import org.springframework.samples.foorder.model.PlatoPedido;
import org.springframework.samples.foorder.model.PlatoPedidoDTO;
import org.springframework.stereotype.Component;

@Component
public class PlatoPedidoConverter {
	
	public PlatoPedido convertPPDTOToEntity(PlatoPedidoDTO pp) {
		PlatoPedido res = new PlatoPedido();
		BeanUtils.copyProperties(pp, res);     //Obviar ids de relaciones en un futuro o establecer en null la relacion
		return res;	
	}
	
}
