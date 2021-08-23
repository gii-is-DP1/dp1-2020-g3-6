package org.springframework.samples.foorder.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.foorder.model.Camarero;
import org.springframework.samples.foorder.model.Comanda;
import org.springframework.samples.foorder.model.PlatoPedido;
import org.springframework.samples.foorder.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@ExtendWith(MockitoExtension.class)
class ComandaServiceTests {                
	@Autowired
	protected ComandaService comandaService;
	@Autowired
	protected PlatoPedidoService ppService;
	@Autowired
	protected CamareroService camareroService;
	
	private Principal principal;
	@Test
	void shouldFindComandasByDia() {
		Collection<Comanda> comanda = comandaService.encontrarComandaDia("2021-02-09");
		assertThat(comanda.size()).isEqualTo(1);

		comanda = comandaService.encontrarComandaDia("2020-02-09");
		assertThat(comanda.isEmpty()).isTrue();
	}
	
	@Test
	@Transactional
	public void shouldInsertComanda() {
		Collection<Comanda> comandas = comandaService.encontrarComandaDia(LocalDate.now().toString());
		int found = comandas.size();

		Comanda comanda = new Comanda();
		comanda.setFechaCreado(LocalDateTime.now());
		comanda.setFechaFinalizado(LocalDateTime.now().plusHours(1));
		comanda.setMesa(17);
		comanda.setPrecioTotal(45.00);
		Camarero camarero = new Camarero();
				camarero.setName("Pedro");
				camarero.setApellido("Varo");
				camarero.setGmail("pedvar@gmail.com");
				camarero.setTelefono("654654659");
				camarero.setUsuario("TestComanda1");
				camarero.setContrasena("12345");
				camareroService.save(camarero);
		comanda.setCamarero(camarero);
              
		comandaService.save(comanda);
		assertThat(comanda.getId().longValue()).isNotEqualTo(0);

		comandas = comandaService.encontrarComandaDia(LocalDate.now().toString());
		assertThat(comandas.size()).isEqualTo(found + 1);
	}
	
	@Test
	@Transactional
	void shouldFindComandasActuales() {
		Collection<Comanda> comandas = comandaService.encontrarComandaDia(LocalDate.now().toString());
		int found = comandas.size();

		Comanda comanda = new Comanda();
		comanda.setFechaCreado(LocalDateTime.now());
		comanda.setFechaFinalizado(null);
		comanda.setMesa(20);
		comanda.setPrecioTotal(45.00);
		Camarero camarero = new Camarero();
				camarero.setName("Fernando");
				camarero.setApellido("Hidalgo");
				camarero.setGmail("ferhid@gmail.com");
				camarero.setTelefono("683683683");
				camarero.setUsuario("TestComanda2");
				camarero.setContrasena("12345");
				camareroService.save(camarero);
		comanda.setCamarero(camarero);
              
		comandaService.save(comanda);
		assertThat(comanda.getId().longValue()).isNotEqualTo(0);

		comandas = comandaService.encontrarComandaDia(LocalDate.now().toString());
		assertThat(comandas.size()).isEqualTo(found + 1);
	}
	
	@Test
	@Transactional
	void shouldGetPlatosPedidos() {
		Collection<Comanda> comandas = comandaService.encontrarComandaActual();
		
		Comanda comanda = comandas.stream().findFirst().get();
		int res = comandaService.getPlatosPedidoDeComanda(comanda.getId()).size();
		assertThat(res).isEqualTo(3);
	}
	
	@Test
	@Transactional
	void shouldFindLastId() {
		Integer lastId = comandaService.findLastId();
		
		Comanda comanda = new Comanda();
		comanda.setFechaCreado(LocalDateTime.now());
		comanda.setFechaFinalizado(LocalDateTime.now().plusHours(1));
		comanda.setMesa(8);
		comanda.setPrecioTotal(45.00);
		Camarero camarero = new Camarero();
				camarero.setName("Maria");
				camarero.setApellido("Zamudio");
				camarero.setGmail("marzam@gmail.com");
				camarero.setTelefono("632632637");
				camarero.setUsuario("TestComanda3");
				camarero.setContrasena("12345");
				camareroService.save(camarero);
		comanda.setCamarero(camarero);
              
		comandaService.save(comanda);
		assertThat(comanda.getId().longValue()).isNotEqualTo(0);
		assertThat(comandaService.findLastId()).isEqualTo(lastId + 1);
	}

	@Test
	void shouldUpdateComanda() {
		Collection<Comanda> comandas = comandaService.encontrarComandaActual();
		int found = comandas.size();
		
		//Ahora cerramos una comanda ya abierta en la BD
		Comanda comanda = comandaService.findById(2).get();
		comanda.setFechaFinalizado(LocalDateTime.now());

		assertThat(comandaService.encontrarComandaActual().size()).isEqualTo(found-1);
	}
	
	@Test
	void shouldInstanciateComanda() {
		principal=Mockito.mock(Principal.class);
		Comanda comanda = new Comanda();
		comanda.setFechaCreado(LocalDateTime.now());
		comanda.setFechaFinalizado(LocalDateTime.now().plusHours(1));
		comanda.setMesa(17);
		comanda.setPrecioTotal(45.00);
		User user= new User();
		user.setUsername("Pedro");
		user.setPassword("123456");
		Comanda com=this.comandaService.instanciarComanda(comanda, principal);

		assertEquals(com.getMesa(), comanda.getMesa());
	}
	
	@Test
	void shouldFindAll() {
		List<Comanda> list=(List<Comanda>) this.comandaService.findAll();
		assertThat(list.size()).isEqualTo(2);
	}
	
	@Test
	public void shouldAnadirComandaAPlato(){
		Optional<Comanda>opCom=this.comandaService.findById(1);
		List<PlatoPedido>l=new ArrayList<PlatoPedido>(opCom.get().getPlatosPedidos());
		PlatoPedido p1=l.get(0);
		Integer mesa= p1.getComanda().getMesa();
		this.comandaService.anadirComandaAPlato(p1, 1);
		assertEquals(mesa, p1.getComanda().getMesa());
	}
	
	@Test
	public void shouldEstaFinalizado(){
		Boolean res= this.comandaService.estaFinalizado(this.comandaService.findById(1).get());
		assertTrue(res);
	}
}
