package org.springframework.samples.foorder.web;

import org.springframework.beans.BeanUtils;
import org.springframework.samples.foorder.model.Producto;
import org.springframework.samples.foorder.model.ProductoDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductoConverter {
	
	public Producto convertProductoDTOToEntity(ProductoDTO producto) {
		Producto res = new Producto();
		BeanUtils.copyProperties(producto, res);
		return res;	
	}
	
	public ProductoDTO convertEntityToProductoDTO(Producto producto) {
		ProductoDTO res = new ProductoDTO();
		BeanUtils.copyProperties(producto, res);
		res.setProveedor(producto.getProveedor().getName());
		return res;	
	}
}
