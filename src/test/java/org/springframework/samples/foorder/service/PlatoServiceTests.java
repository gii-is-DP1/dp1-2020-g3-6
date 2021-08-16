package org.springframework.samples.foorder.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.foorder.model.Plato;
import org.springframework.samples.foorder.service.exceptions.PlatoEnProcesoException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PlatoServiceTests {

	@Autowired
	private PlatoService platoService;
	
	@Autowired
	private ProductoService productoService;
	
	@Test
	public void shouldDeleteById() throws PlatoEnProcesoException{
		List<Plato> lp1 = new ArrayList<Plato>(platoService.findAll());
		int lp1Size = lp1.size();
		assertEquals(11, lp1Size);
		
		platoService.deleteById(2);
		
		List<Plato> lp2 = new ArrayList<Plato>(platoService.findAll());
		int lp2Size = lp2.size();
		assertEquals(lp1Size-1, lp2Size);
	}
	
	@Test
	public void shouldFindAllAvailable() {
		List<Plato> lplato1 = platoService.findAllAvailable();
		int lp1_size = lplato1.size();
		assertEquals(10, lp1_size);
		
		productoService.findById(3).get().setCantAct(0.);
		
		List<Plato> lplato2 = platoService.findAllAvailable();
		int lp2_size = lplato2.size();
		assertEquals(lp1_size-1, lp2_size);
				
		
	}
	
	@Test
	public void shouldIngredienteRepetido() {		
		assertEquals(false, platoService.ingredienteEstaRepetido("Lechuga",2));
		assertEquals(false, platoService.ingredienteEstaRepetido("Mayonesa",2));
		assertEquals(true, platoService.ingredienteEstaRepetido("Huevos", 2));
	}
		
	
}
