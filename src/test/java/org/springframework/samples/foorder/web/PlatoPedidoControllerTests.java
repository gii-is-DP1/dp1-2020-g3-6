package org.springframework.samples.foorder.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.foorder.configuration.SecurityConfiguration;
import org.springframework.samples.foorder.model.Authorities;
import org.springframework.samples.foorder.model.Comanda;
import org.springframework.samples.foorder.model.EstadoPlato;
import org.springframework.samples.foorder.model.Plato;
import org.springframework.samples.foorder.model.PlatoPedido;
import org.springframework.samples.foorder.model.User;
import org.springframework.samples.foorder.service.ComandaService;
import org.springframework.samples.foorder.service.EstadoPlatoService;
import org.springframework.samples.foorder.service.IngredientePedidoService;
import org.springframework.samples.foorder.service.PlatoPedidoService;
import org.springframework.samples.foorder.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(value = PlatoPedidoController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
classes = WebSecurityConfigurer.class),
excludeAutoConfiguration = SecurityConfiguration.class)
public class PlatoPedidoControllerTests {


	private static final int TEST_PLATOPEDIDO_ID = 1;
	private static final int TEST_COMANDA_ID = 1;
	private static final int TEST_COMANDA_FAKE_ID = -1;
	private static final int TEST_PLATOPEDIDO_FAKE_ID = -1;
	private static final int TEST_ING_ID = 1;
	@MockBean
	private PlatoPedidoService platoPedidoService;
	@MockBean
	private IngredientePedidoService ingredientePedidoService;
	@MockBean
	private EstadoPlatoService estadoPlatoService;
	@MockBean
	private PlatoPedidoConverter ppConverter;
	@MockBean
	private EstadoPlatoFormatter estadoPlatoFormatter;
	@MockBean
	private PlatoFormatter platoFormatter;
	@MockBean
	private UserService userService;
	@MockBean
	private ComandaService comandaService;




	@Autowired
	private MockMvc mockMvc;


	private PlatoPedido platoPedido;
	private Plato plato;
	private String username;
	private User user;
	private Comanda comanda;
	@BeforeEach
	void setup() throws ParseException {
		Set<Authorities> authorities= new HashSet<Authorities>();
		Authorities a= new Authorities();
		a.setAuthority("cocinero");
		a.setUser(user);
		authorities.add(a);

		username= "username";

		user= new User();
		plato= new Plato();
		platoPedido = new PlatoPedido();
		platoPedido.setId(1);
		platoPedido.setPlato(plato);

		Iterable<PlatoPedido> pp= new ArrayList<PlatoPedido>();

		comanda= new Comanda();
		given(this.userService.findAuthoritiesByUsername("username")).willReturn("cocinero");
		given(this.platoPedidoService.platosPedidosDisponibles()).willReturn(pp);
		given(this.platoPedidoService.findById(TEST_PLATOPEDIDO_ID)).willReturn(Optional.of(platoPedido));
		given(this.comandaService.findById(TEST_COMANDA_ID)).willReturn(Optional.of(comanda));
		given(this.comandaService.findById(TEST_COMANDA_FAKE_ID)).willReturn(Optional.empty());
		given(this.platoPedidoService.findById(TEST_PLATOPEDIDO_FAKE_ID)).willReturn(Optional.empty());
	}



	@WithMockUser(value = "username")
	@Test
	@DisplayName("Listar Plato pedido")
	void listarPlatosPedidos() throws Exception {
		user.setUsername(username);
		given(this.userService.getUserSession()).willReturn(user);

		mockMvc.perform(get("/platopedido"))
		.andExpect(status().isOk())
		.andExpect(view().name("platosPedido/listaPlatosPedido"))
		.andExpect(model().attributeExists("platopedido"));
	}

	@WithMockUser(value = "username")
	@Test
	@DisplayName("Listar Plato pedido sin tener la autoridad de cocinero")
	void listarPlatosPedidosSinSerCocinero() throws Exception {
		user.setUsername(username);
		given(this.userService.getUserSession()).willReturn(user);
		given(this.userService.findAuthoritiesByUsername("username")).willReturn("camarero");

		mockMvc.perform(get("/platopedido"))
		.andExpect(status().isOk())
		.andExpect(view().name("errors/error-403"))
		.andExpect(model().attributeDoesNotExist("platopedido"));
	}

	@WithMockUser(value = "spring")
	@Test
	void guardarPP() throws Exception {
		given(this.ppConverter.convertPPDTOToEntity(any())).willReturn(new PlatoPedido());
		given(this.estadoPlatoFormatter.parse("ENCOLA", Locale.ENGLISH)).willReturn(new EstadoPlato());
		given(this.platoFormatter.parse("Ensalada", Locale.ENGLISH)).willReturn(new Plato());
		mockMvc.perform(post("/platopedido/{comandaId}/save", TEST_PLATOPEDIDO_ID)
				.with(csrf())
				.param("platodto", "Ensalada"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/platopedido/comanda/"+TEST_COMANDA_ID+"/"+null+"?message=guardado correctamente&comandaId="+TEST_COMANDA_ID));
	}

	@WithMockUser(value = "spring")
	@Test
	void initUpdatePPForm() throws Exception {
		user.setUsername(username);
		given(this.userService.getUserSession()).willReturn(user);
		given(this.userService.findAuthoritiesByUsername("username")).willReturn("camarero");
		mockMvc.perform(get("/platopedido/comanda/{comandaId}/{ppId}", TEST_COMANDA_ID ,TEST_PLATOPEDIDO_ID)
				.with(csrf()))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("platosPedido/modificarIngredientes"));
	}

	@WithMockUser(value = "spring")
	@Test
	void initUpdatePPFormFakeUser() throws Exception {
		user.setUsername(username);
		given(this.userService.getUserSession()).willReturn(user);
		given(this.userService.findAuthoritiesByUsername("username")).willReturn("manager");
		mockMvc.perform(get("/platopedido/comanda/{comandaId}/{ppId}", TEST_COMANDA_ID ,TEST_PLATOPEDIDO_ID)
				.with(csrf()))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("errors/error-403"));
	}

	@WithMockUser(value = "spring")
	@Test
	void initUpdatePPFormComandaEmpty() throws Exception {
		user.setUsername(username);
		given(this.userService.getUserSession()).willReturn(user);
		given(this.userService.findAuthoritiesByUsername("username")).willReturn("camarero");
		mockMvc.perform(get("/platopedido/comanda/{comandaId}/{ppId}", TEST_COMANDA_FAKE_ID ,TEST_PLATOPEDIDO_ID)
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/?message=Esa comanda no existe"));
	}

	@WithMockUser(value = "spring")
	@Test
	void initUpdatePPFormPlatoPedidoEmpty() throws Exception {
		user.setUsername(username);
		given(this.userService.getUserSession()).willReturn(user);
		given(this.userService.findAuthoritiesByUsername("username")).willReturn("camarero");
		mockMvc.perform(get("/platopedido/comanda/{comandaId}/{ppId}", TEST_COMANDA_ID ,TEST_PLATOPEDIDO_FAKE_ID)
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/comanda/listaComandaActual/"+TEST_COMANDA_ID+"?message=Ese plato no existe"));
	}

	@WithMockUser(value = "spring")
	@Test
	void showIngredientePedido() throws Exception {
		mockMvc.perform(get("/platopedido/{ppId}", TEST_PLATOPEDIDO_ID)
				.with(csrf()))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("platosPedido/ingredientesDePlatoPedido"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void guardarIngrediente() throws Exception {
		mockMvc.perform(post("/platopedido/{comandaId}/guardarIngrediente/{ppId}/{ingId}", TEST_COMANDA_ID ,TEST_PLATOPEDIDO_ID,TEST_ING_ID)
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/platopedido/comanda/"+TEST_COMANDA_ID+"/"+TEST_PLATOPEDIDO_ID+"?message=Insertado"));


	}
	
	@WithMockUser(value = "spring")
	@Test
	void Stock() throws Exception {
		user.setUsername(username);
		given(this.userService.getUserSession()).willReturn(user);
		given(this.userService.findAuthoritiesByUsername("username")).willReturn("cocinero");
		given(this.estadoPlatoFormatter.parse("ENCOLA", Locale.ENGLISH)).willReturn(new EstadoPlato());
		mockMvc.perform(get("/platopedido/modificarEstado/{platopedidoID}/{cambiarA}", TEST_PLATOPEDIDO_ID ,"ENCOLA")
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/platopedido?message=Se ha cambiado el plato con exito"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void StockNoCocinero() throws Exception {
		user.setUsername(username);
		given(this.userService.getUserSession()).willReturn(user);
		given(this.userService.findAuthoritiesByUsername("username")).willReturn("camarero");
		given(this.estadoPlatoFormatter.parse("ENCOLA", Locale.ENGLISH)).willReturn(new EstadoPlato());
		mockMvc.perform(get("/platopedido/modificarEstado/{platopedidoID}/{cambiarA}", TEST_PLATOPEDIDO_ID ,"ENCOLA")
				.with(csrf()))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("errors/error-403"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void StockPlatoPedidoNoPresente() throws Exception {
		user.setUsername(username);
		given(this.userService.getUserSession()).willReturn(user);
		given(this.userService.findAuthoritiesByUsername("username")).willReturn("cocinero");
		given(this.estadoPlatoFormatter.parse("ENCOLA", Locale.ENGLISH)).willReturn(new EstadoPlato());
		mockMvc.perform(get("/platopedido/modificarEstado/{platopedidoID}/{cambiarA}", TEST_PLATOPEDIDO_FAKE_ID ,"ENCOLA")
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/platopedido?message=No se ha cambiado el plato con exito"));
	}
}
