package org.springframework.samples.foorder.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.foorder.model.LineaPedido;
import org.springframework.samples.foorder.model.Pedido;
import org.springframework.samples.foorder.model.Producto;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class LineaPedidoServiceTests {
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private PedidoService pedidoService;
	
	@Autowired
	private LineaPedidoService lineaPedidoService;
	
	//AñadirLineaPedido
	@Test
	public void añadirLineaPedidoAPedido() {

		Producto producto = productoService.findById(5).get();
		Pedido pedido1 = pedidoService.findById(1).get();
		
		LineaPedido lineapedido = lineaPedidoService.anadirLineaPedido(producto, pedido1);
		lineaPedidoService.save(lineapedido);
		assertThat(lineapedido.getId()).isNotNull();
		
		double cantidad = producto.getCantMax()-producto.getCantAct();
		assertThat(lineapedido.getCantidad()).isEqualTo(cantidad);	
	}
}
