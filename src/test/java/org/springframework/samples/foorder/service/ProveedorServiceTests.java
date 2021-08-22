package org.springframework.samples.foorder.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.foorder.model.Proveedor;
import org.springframework.stereotype.Service;

//tambien testea pedido y linea pedido

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProveedorServiceTests {
	@Autowired
	private ProveedorService proveedorService;
	
	private Proveedor proveedor;
	
	@BeforeEach                                         
    public void setUp() throws Exception {
		proveedor = new Proveedor();
		proveedor.setName("Proveedor");
    }
	
	@Test
	public void shouldSaveProveedor() {
		int n = proveedorService.findAllNames().size();
		proveedorService.save(proveedor);
		int m = proveedorService.findAllNames().size();
		
		assertThat(m).isEqualTo(n+1);
	}
	
	@Test
	public void shouldUpdateProveedor() {
		proveedorService.save(proveedor);
		proveedor.setGmail("email@email.com");
		int m = proveedorService.findAllNames().size();
		proveedorService.save(proveedor);
		int n = proveedorService.findAllNames().size();
		assertThat(m).isEqualTo(n);
	}
	
	@Test
	public void shouldUnhideProveedor() {
		proveedor.setActivo(false);
		proveedorService.save(proveedor);
		proveedorService.unhide(proveedor);
		boolean aux = proveedorService.findByName(proveedor.getName()).get().getActivo();
		assertThat(aux).isEqualTo(true);
		
	}
	
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

	@Test
	public void findAllTest() {	
		List<Proveedor> ls=(List<Proveedor>) proveedorService.findAll();
		assertEquals(ls.size(), 4);
		
	}
	
	@Test
	public void hideTest() {	
		Proveedor prov= this.proveedorService.findById(1).get();
		this.proveedorService.hide(prov);
		assertEquals(prov.getActivo(), false);
		
	}
	
}
