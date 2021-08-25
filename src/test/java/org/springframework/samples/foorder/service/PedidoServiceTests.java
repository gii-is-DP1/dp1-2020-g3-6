package org.springframework.samples.foorder.service;

import static org.junit.Assert.assertEquals;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.foorder.model.Pedido;
import org.springframework.samples.foorder.service.exceptions.DuplicatedPedidoException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))

public class PedidoServiceTests {

	@Autowired
	private ProveedorService proveedorService;
	
	@Autowired
	private PedidoService pedidoService;
	
	@Test
	public void shouldSaveTest() {
		int found = pedidoService.count();
		
		Pedido pedido = new Pedido(LocalDate.now(), LocalDate.now(), 23., false, proveedorService.findById(3).get());
		
		try {
			pedidoService.save(pedido);
		} catch (DuplicatedPedidoException ex) {
			Logger.getLogger(PedidoServiceTests.class.getName()).log(Level.SEVERE, null, ex);
		}
      
		int size = pedidoService.count();
		assertEquals(found+1, size);	
	}
	
	@Test
	public void shouldntSaveRepetido() {
		int found = pedidoService.count();
		
		Pedido pedido = new Pedido();
		pedido.setFechaPedido(LocalDate.now());
		pedido.setHaLlegado(false);
		pedido.setProveedor(proveedorService.findById(1).get());
		
		try {
			pedidoService.save(pedido);
		} catch (DuplicatedPedidoException ex) {
			Logger.getLogger(PedidoServiceTests.class.getName()).log(Level.SEVERE, null, ex);
		}
      
		int size = pedidoService.count();
		assertEquals(found, size);
	}
	
	@Test
	public void shouldCrearPedido() throws DataAccessException, DuplicatedPedidoException{
		pedidoService.crearPedido(10);
		List<Pedido> lp = new ArrayList<Pedido>();
		Iterator<Pedido> itp = pedidoService.findAll().iterator();
		while(itp.hasNext()) {
			lp.add(itp.next());
		}
		assertEquals(3, lp.size());
		
	}
	
	@Test
	public void shouldRecargarStock() throws DataAccessException, DuplicatedPedidoException{
		Pedido p = pedidoService.findById(1).get();
		assertEquals(false, p.getHaLlegado());
		assertEquals(null, p.getFechaEntrega());
		
		pedidoService.recargarStock(1);
		
		p = pedidoService.findById(1).get();
		assertEquals(true, p.getHaLlegado());
		assertEquals(LocalDate.now(), p.getFechaEntrega());
	}
	
	@Test
	public void shouldEncontrarPedidoDia() {
		List<Pedido> lp = new ArrayList<Pedido>(pedidoService.encontrarPedidoDia("2021-02-04"));
		assertEquals(1, lp.size());
		
		Pedido p = pedidoService.findById(1).get();
		assertEquals(p, lp.get(0));
	
		List<Pedido> lp2 = new ArrayList<Pedido>(pedidoService.encontrarPedidoDia("2021-02-05"));
		assertEquals(1, lp.size());
		
		Pedido p2 = pedidoService.findById(2).get();
		assertEquals(p2, lp2.get(0));
		
	
	}
	
}
