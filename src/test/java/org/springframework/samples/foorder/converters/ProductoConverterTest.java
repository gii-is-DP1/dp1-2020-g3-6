package org.springframework.samples.foorder.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.foorder.model.Producto;
import org.springframework.samples.foorder.model.ProductoDTO;
import org.springframework.samples.foorder.model.Proveedor;
import org.springframework.samples.foorder.model.TipoProducto;
import org.springframework.samples.foorder.web.ProductoConverter;

@ExtendWith(MockitoExtension.class)
public class ProductoConverterTest {
	
	@Mock
	private ProductoConverter productoConverter;
	
	private Producto producto;
	
	private ProductoDTO prDTO;
	
	@BeforeEach
	void setup() {
		
		productoConverter= new ProductoConverter();
		
		Proveedor proveedor= new Proveedor();
		proveedor.setName("pedro");
		TipoProducto tp= new TipoProducto();
		tp.setId(1);
		tp.setName("carne");
		
		producto= new Producto();
		
		producto.setName("albondigas");
		producto.setProveedor(proveedor);
		producto.setTipoProducto(tp);
		producto.setCantMin(1.);
		producto.setCantAct(2.);
		producto.setCantMax(3.);
		
		prDTO = new ProductoDTO();
		prDTO.setName("albondigas");
		prDTO.setProveedor(proveedor.getName());
		prDTO.setTipoproductodto(tp.getName());
		prDTO.setCantMin(1.);
		prDTO.setCantAct(2.);
		prDTO.setCantMax(3.);
		
	}
	
	@Test
	void testConvertEntityToDTO() {
		ProductoDTO prDTO=this.productoConverter.convertEntityToProductoDTO(producto);
		assertEquals(prDTO.getName(), producto.getName());
		assertEquals(prDTO.getProveedor(), producto.getProveedor().getName());
		assertEquals(prDTO.getTipoproductodto(), producto.getTipoProducto().getName());
		assertEquals(prDTO.getCantMin(), producto.getCantMin());
		assertEquals(prDTO.getCantAct(), producto.getCantAct());
		assertEquals(prDTO.getCantMax(), producto.getCantMax());
	}
	
	@Test
	void testConvertDTOToEntity() {
		Producto pr1=this.productoConverter.convertProductoDTOToEntity(prDTO);
		assertEquals(prDTO.getName(), pr1.getName());
		assertEquals(prDTO.getCantMin(), pr1.getCantMin());
		assertEquals(prDTO.getCantAct(), pr1.getCantAct());
		assertEquals(prDTO.getCantMax(), pr1.getCantMax());
	}

}
