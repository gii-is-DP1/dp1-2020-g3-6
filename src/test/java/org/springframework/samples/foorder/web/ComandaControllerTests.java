package org.springframework.samples.foorder.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.foorder.configuration.SecurityConfiguration;
import org.springframework.samples.foorder.model.Comanda;
import org.springframework.samples.foorder.model.EstadoPlato;
import org.springframework.samples.foorder.model.Ingrediente;
import org.springframework.samples.foorder.model.IngredientePedido;
import org.springframework.samples.foorder.model.PlatoPedido;
import org.springframework.samples.foorder.service.CamareroService;
import org.springframework.samples.foorder.service.ComandaService;
import org.springframework.samples.foorder.service.PlatoPedidoService;
import org.springframework.samples.foorder.service.PlatoService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers=ComandaController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
public class ComandaControllerTests {
	
	private static final int TEST_COMANDA_ID = 1;
	private static final int TEST_PLATOPEDIDO_ID = 1;
	private static final int TEST_PLATOPEDIDOVACIO_ID = 1;
	private static final int TEST_COMANDA_FAKE_ID = -1;
	
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ComandaService comandaService;

	@MockBean
	private PlatoPedidoService platoPedidoService;
	
	@MockBean
	private CamareroService camareroService;
	
	@MockBean
	private PlatoService platoService;


	private Comanda comanda;
	private IngredientePedido ingredientePedido;
	private PlatoPedido platoPedido;
	private Ingrediente ingrediente;
	private PlatoPedido platoPedidoVacio;	
	private Principal user;
	private EstadoPlato estadoPlato;
	private List<IngredientePedido> l=new ArrayList<IngredientePedido>();

	@BeforeEach
	void test() {
	
	
		
		comanda = new Comanda();
		comanda.setId(TEST_COMANDA_ID);
		comanda.setMesa(30);
		comanda.setFechaCreado(LocalDateTime.now());
		comanda.setFechaFinalizado(null);
		comanda.setPrecioTotal(5.0);
		
		platoPedido = new PlatoPedido();
		platoPedido.setId(TEST_PLATOPEDIDO_ID);
		
		platoPedido.setEstadoplato(estadoPlato);
		
		platoPedidoVacio = new PlatoPedido();
		platoPedidoVacio.setId(TEST_PLATOPEDIDOVACIO_ID);
		
		ingredientePedido = new IngredientePedido();
		ingredientePedido.setId(30);
		ingredientePedido.setCantidadPedida(5.0);
		ingredientePedido.setIngrediente(ingrediente);
		ingredientePedido.setPp(platoPedido);
		
		
		platoPedido.setIngredientesPedidos(l);
		
		given(this.platoPedidoService.findById(TEST_PLATOPEDIDO_ID)).willReturn(Optional.of(platoPedido));
		given(this.comandaService.findById(TEST_COMANDA_ID)).willReturn(Optional.of(comanda));

	}

	@WithMockUser(value = "propietario")
	@Test
	@DisplayName("Listar Camarero")
	void showCamarerosList() throws Exception {
		mockMvc.perform(get("/comanda/listaComandaTotal")).andExpect(status().isOk())
		.andExpect(view()
		.name("comanda/listaComandaTotal"))
		.andExpect(model().attributeExists("comanda"));
	}
	
	@WithMockUser(value = "propietario")
	@Test
	@DisplayName("Vista de Propietario para la lista de Comandas de UN DIA concreto")
	void testlistadoComandaDia() throws Exception {
		mockMvc.perform(get("/comanda/listaComandaTotal/dia"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view()
		.name("comanda/listaComandaTotal"));
	}
	
	@WithMockUser(value = "propietario")
	@Test
	@DisplayName("Vista de Propietario para la lista de Comandas de UN DIA nulo")
	void testlistadoComandaDiaNegative() throws Exception {
		mockMvc.perform(get("/comanda/listaComandaTotal/dia")
		.param("date", ""))
		.andExpect(status().is3xxRedirection())
		.andExpect(view()
		.name("redirect:/comanda/listaComandaTotal?message=Debes elegir un dia obligatoriamente"));
	}
	
	@WithMockUser(value = "propietario")
	@Test
	@DisplayName("Vista de Propietario para la lista de Comandas actuales")
	void testlistadoComandaActual() throws Exception {
		mockMvc.perform(get("/comanda/listaComandaActual"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view()
		.name("comanda/listaComandaActual"));
	}
	
	@WithMockUser(value = "propietario")
	@Test
	@DisplayName("Vista de Camarero para la lista actual de Comandas sin finalizar")
	void finalizarComanda() throws Exception {
		given(this.comandaService.estaFinalizado(comanda)).willReturn(true);
		mockMvc.perform(get("/comanda/listaComandaActual/finalizarComanda/{comandaID}",TEST_COMANDA_ID))
		.andExpect(status().is3xxRedirection())
		.andExpect(view()
		.name("redirect:/comanda/listaComandaActual?message=Esta comanda aún tiene platos por finalizar"));
	}
	
	@WithMockUser(value = "propietario")
	@Test
	@DisplayName("Vista de Camarero para la lista actual de Comandas con mensaje La comanda se ha finalizado correctamente")
	void finalizarComanda2Message() throws Exception {
		given(this.comandaService.estaFinalizado(comanda)).willReturn(false);
		mockMvc.perform(get("/comanda/listaComandaActual/finalizarComanda/{comandaID}",TEST_COMANDA_ID))
		.andExpect(status().is3xxRedirection())
		.andExpect(view()
		.name("redirect:/comanda/listaComandaActual?message=La comanda se ha finalizado correctamente"));
	}	
	
	@WithMockUser(value = "propietario")
	@Test
	@DisplayName("Vista de Camarero para la lista actual de Comandas con mensaje La comanda ya está finalizada")
	void finalizarComanda3Message() throws Exception {
		comanda.setFechaFinalizado(LocalDateTime.now());
		given(this.comandaService.findById(TEST_COMANDA_ID)).willReturn(Optional.of(comanda));
		given(this.comandaService.estaFinalizado(comanda)).willReturn(false);
		mockMvc.perform(get("/comanda/listaComandaActual/finalizarComanda/{comandaID}",TEST_COMANDA_ID))
		.andExpect(status().is3xxRedirection())
		.andExpect(view()
		.name("redirect:/comanda/listaComandaActual?message=La comanda ya está finalizada"));
	}	
	
	@WithMockUser(value = "propietario")
	@Test
	@DisplayName("Vista de Camarero para la lista actual de Comandas con mensaje La comanda pedida no existe")
	void finalizarComanda4Message() throws Exception {
		given(this.comandaService.findById(TEST_COMANDA_ID)).willReturn(Optional.empty());
		mockMvc.perform(get("/comanda/listaComandaActual/finalizarComanda/{comandaID}",TEST_COMANDA_ID))
		.andExpect(status().is3xxRedirection())
		.andExpect(view()
		.name("redirect:/comanda/listaComandaActual?message=La comanda pedida no existe"));
	}	
	
	@WithMockUser(value = "propietario")
	@Test
	@DisplayName("Vista de Camarero para la lista de platos de una comanda con fake id")
	void testInfoComandaFake() throws Exception {
		mockMvc.perform(get("/comanda/listaComandaActual/{comandaID}",TEST_COMANDA_FAKE_ID))
		.andExpect(status().is3xxRedirection())
		.andExpect(view()
		.name("redirect:/comanda/listaComandaActual?message=La comanda pedida no existe"));
	}	
	
	@WithMockUser(value = "propietario")
	@Test
	@DisplayName("Vista de Camarero para la lista de platos de una comanda exitosa")
	void testInfoComanda() throws Exception {
		mockMvc.perform(get("/comanda/listaComandaActual/{comandaID}",TEST_COMANDA_ID))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view()
		.name("comanda/comandaDetails"));
	}	
	
	@WithMockUser(value = "propietario")
	@Test
	@DisplayName("Vista de Camarero para crear comanda")
	void crearComanda() throws Exception {
		given(this.comandaService.instanciarComanda(comanda, user)).willReturn(comanda);
		given(this.comandaService.findLastId()).willReturn(TEST_COMANDA_ID);
		mockMvc.perform(post("/comanda/listaComandaActual/new")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("mesa", "1"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view()
		.name("redirect:/comanda/listaComandaActual/"+TEST_COMANDA_ID));
	}	
	
	@WithMockUser(value = "propietario")
	@Test
	@DisplayName("Vista de Camarero para crear comanda con errores")
	void crearComandaError() throws Exception {
		given(this.comandaService.instanciarComanda(comanda, user)).willReturn(comanda);
		given(this.comandaService.findLastId()).willReturn(TEST_COMANDA_ID);
		mockMvc.perform(post("/comanda/listaComandaActual/new")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("mesa", "-1"))
		.andExpect(model().attributeHasErrors("comanda"))
		.andExpect(model().attributeHasFieldErrors("comanda", "mesa"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view()
		.name("comanda/listaComandaActual"));
	}	
	
	@WithMockUser(value = "propietario")
	@Test
	@DisplayName("test para asignar una comanda con fallo")
	void asignarComandaFailed() throws Exception {
		mockMvc.perform(post("/comanda/listaComandaActual/asignar/{comandaId}/{ppId}",TEST_COMANDA_ID,TEST_PLATOPEDIDO_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view()
		.name("redirect:/platopedido/comanda/"+TEST_COMANDA_ID+"/"+TEST_PLATOPEDIDO_ID+"?message=Ha habido un error al guardar, no se han seleccionado ingredientes"));
	}	
	
	@WithMockUser(value = "propietario")
	@Test
	@DisplayName("test para asignar una comanda sin fallos")
	void asignarComandaSuccess() throws Exception {
		l.add(ingredientePedido);
		platoPedido.setIngredientesPedidos(l);
		mockMvc.perform(post("/comanda/listaComandaActual/asignar/{comandaId}/{ppId}",TEST_COMANDA_ID,TEST_PLATOPEDIDO_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view()
		.name("redirect:/comanda/listaComandaActual/"+TEST_COMANDA_ID));
	}	

	
	
	

}
