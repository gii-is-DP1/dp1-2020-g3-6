package org.springframework.samples.foorder.service;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.foorder.model.Proveedor;
import org.springframework.samples.foorder.service.ProveedorService;
import org.springframework.stereotype.Service;

//tambien testea pedido y linea pedido

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProveedorServiceTests {
	@Autowired
	private ProveedorService proveedorService;
	
	
	//FindAllNames
	@Test
	public void listaDeNombresDeProveedores() {
		Collection<String> nombres = proveedorService.findAllNames();
		assertEquals(4, nombres.size());
	}
	
	//FindProveedorByName
	@Test
	public void esBuscarProveedor() {
		Proveedor test = proveedorService.findByName("Makro").get();
		assertEquals("Makro", test.getName());
	}
	
	@Test
	public void findAllNames() {		
		assertEquals(proveedorService.findAllNames().size(),4);
	}
	
	@Test
	public void findActivosNameTest() {	
		assertEquals(proveedorService.findActivosName().size(), 4);
		
	}
	
	@Test
	public void findActivosTest() {
		String name=proveedorService.findActivos()
				.iterator().next().getName();
		assertEquals(name, "Makro");
		
	}
	
	@Test
	public void findProveedorByNameTest() {	
		int id=proveedorService.findByName("Makro").get().getId();
		assertEquals(id, 1);
		
	}

}
