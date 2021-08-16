package org.springframework.samples.foorder.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.foorder.configuration.SecurityConfiguration;
import org.springframework.samples.foorder.model.Cocinero;
import org.springframework.samples.foorder.repository.CocineroRepository;
import org.springframework.samples.foorder.service.AuthoritiesService;
import org.springframework.samples.foorder.service.CocineroService;
import org.springframework.samples.foorder.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(value = CocineroController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class CocineroControllerTests {

	private static final int TEST_COCINERO_ID = 1;
	private static final int TEST_COCINERO_FAKE_ID = -1;

	@MockBean
	private CocineroService cocineroService;

	@MockBean
	private UserService userService;
	
	@MockBean
	private AuthoritiesService authoritiesService;

	@MockBean
	private CocineroRepository cocineroRepository;
	
	@Autowired
	private MockMvc mockMvc;
	
	

	@BeforeEach
	void setup() {
		Cocinero cocinero = new Cocinero();
		cocinero.setApellido("ffffff");
		cocinero.setContrasena("12345");
		cocinero.setGmail("xxxxx@gmail.com");
		cocinero.setName("aaaass");
		cocinero.setTelefono("123456789");
		cocinero.setUsuario("jose");
		//cocinero.getUsuario().se
		given(this.cocineroService.findById(TEST_COCINERO_ID)).willReturn(Optional.of(cocinero));
		
		given(this.cocineroService.findAll()).willReturn(new ArrayList<Cocinero>());
		given(this.cocineroService.findById(TEST_COCINERO_ID)).willReturn(Optional.of(cocinero));


	}

	@WithMockUser(value = "spring")
	@Test
	void showCocinerosList() throws Exception {
		mockMvc.perform(get("/cocinero")).andExpect(status().isOk()).andExpect(view().name("cocinero/listaCocinero"))
				.andExpect(model().attributeExists("cocinero"));
	}

	@WithMockUser(value = "spring")

	@Test
	void initCreationForm() throws Exception {
		mockMvc.perform(get("/cocinero/new")).andExpect(status().isOk())
				.andExpect(view().name("cocinero/editCocinero")).andExpect(model().attributeExists("cocinero"));
	}

	@WithMockUser(value = "spring")

	@Test
	void processCreationCocineroSuccess() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/cocinero/save").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "Pepe")
				.param("apellido", "esc")
				.param("telefono", "543972343")
				.param("gmail", "pe2000@gmail.com")
				.param("contrasena", "12345abvM")
				.param("usuario", "jose")
				.with(csrf())).andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/cocinero?message=Guardado Correctamente"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void processCreationCocineroHasErrors() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/cocinero/save").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "Pe")
				.param("apellido", "es")
				.param("telefono", "543972343")
				.param("gmail", "co2000")
				.param("contrasena", "abcd")
				.param("usuario", "co")
				.with(csrf())).andExpect(status().isOk())
		.andExpect(model().attributeHasErrors("cocinero"))
		.andExpect(model().attributeHasFieldErrors("cocinero", "name"))
		.andExpect(model().attributeHasFieldErrors("cocinero", "apellido"))
		.andExpect(model().attributeHasFieldErrors("cocinero", "gmail"))
		.andExpect(model().attributeHasFieldErrors("cocinero", "usuario"))
		.andExpect(model().attributeHasFieldErrors("cocinero", "contrasena"))
		.andExpect(view().name("cocinero/editCocinero"));
	}
	
	
	@WithMockUser(value = "spring")
	@Test
	void testDeleteCocinero() throws Exception {
		mockMvc.perform(get("/cocinero/delete/{cocineroId}", TEST_COCINERO_ID)
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/cocinero?message=Borrado Correctamente"));
	}


	@WithMockUser(value = "spring")
	@Test
	void testDeleteFakecocinero() throws Exception {
		mockMvc.perform(get("/cocinero/delete/{cocineroId}", TEST_COCINERO_FAKE_ID)
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/cocinero?message=Cocinero no encontrado"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdatecocineroForm() throws Exception {
		mockMvc.perform(get("/cocinero/edit/{cocineroId}", TEST_COCINERO_ID)
				.with(csrf()))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("cocinero/editarCocinero"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateFakecocineroForm() throws Exception {
		mockMvc.perform(get("/cocinero/edit/{cocineroId}", TEST_COCINERO_FAKE_ID)
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/cocinero?message=Ese cocinero no existe"));
	}

	@WithMockUser(value = "spring")
	@Test
	void processEditcocineroSuccess() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/cocinero/edit").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "Pepe")
				.param("apellido", "esc")
				.param("telefono", "543972343")
				.param("gmail", "pe2000@gmail.com")
				.param("contrasena", "12345abvM")
				.param("usuario", "jose")
				.with(csrf())).andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/cocinero?message=Cocinero actualizado"));
	}


	@WithMockUser(value = "spring")
	@Test
	void processEditcocineroHasErrors() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/cocinero/edit").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "Vi")
				.param("apellido", "es")
				.param("telefono", "543972343")
				.param("gmail", "vi2000")
				.param("contrasena", "12345")
				.param("usuario", "vi")
				.with(csrf())).andExpect(status().isOk())
		.andExpect(model().attributeHasErrors("cocinero"))
		.andExpect(model().attributeHasFieldErrors("cocinero", "name"))
		.andExpect(model().attributeHasFieldErrors("cocinero", "apellido"))
		.andExpect(model().attributeHasFieldErrors("cocinero", "gmail"))
		.andExpect(model().attributeHasFieldErrors("cocinero", "usuario"))
		.andExpect(model().attributeHasFieldErrors("cocinero", "contrasena"))
		.andExpect(view().name("cocinero/editarCocinero"));

	}
}
