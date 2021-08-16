package org.springframework.samples.foorder.service;

import java.util.Collection;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.foorder.model.Producto;
import org.springframework.samples.foorder.model.Proveedor;
import org.springframework.samples.foorder.model.TipoProducto;
import org.springframework.samples.foorder.service.exceptions.PedidoPendienteException;
import org.springframework.samples.foorder.service.exceptions.PlatoPedidoPendienteException;
import org.springframework.stereotype.Service;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProductoServiceTests {

	@Autowired
	private ProductoService productoService;


	@Test
	void shouldFindAllProducto() {
		Collection<Producto> productos = Lists.newArrayList(productoService.findAll());
		assertThat(productos.size()).isEqualTo(19);
	}
	
	@Test
	void shouldDeleteProducto() throws DataAccessException, PedidoPendienteException, PlatoPedidoPendienteException {

		int sizeBefore = Lists.newArrayList(this.productoService.findAll()).size();

		productoService.deleteById(3);

		int sizeAfter = Lists.newArrayList(this.productoService.findAll()).size();
		assertEquals(sizeBefore-1, sizeAfter);
	}
	
}
