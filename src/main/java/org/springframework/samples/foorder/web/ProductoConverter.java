package org.springframework.samples.foorder.web;

import org.springframework.beans.BeanUtils;
import org.springframework.samples.foorder.model.Producto;
import org.springframework.samples.foorder.model.ProductoDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductoConverter {
	
	public Producto convertProductoDTOToEntity(ProductoDTO producto) {
		Producto res = new Producto();
		BeanUtils.copyProperties(producto, res);     //Obviar ids de relaciones en un futuro o establecer en null la relacion
		return res;	
	}
	
	public ProductoDTO convertEntityToProductoDTO(Producto producto) {
		ProductoDTO res = new ProductoDTO();
		BeanUtils.copyProperties(producto, res);     //Obviar ids de relaciones en un futuro o establecer en null la relacion
		res.setProveedor(producto.getProveedor().getName());
		res.setTipoproductodto(producto.getTipoProducto().getName());
		return res;	
	}
}
