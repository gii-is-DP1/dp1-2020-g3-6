
package org.springframework.samples.foorder.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.foorder.configuration.SecurityConfiguration;
import org.springframework.samples.foorder.model.Camarero;
import org.springframework.samples.foorder.model.User;
import org.springframework.samples.foorder.repository.CamareroRepository;
import org.springframework.samples.foorder.service.AuthoritiesService;
import org.springframework.samples.foorder.service.CamareroService;
import org.springframework.samples.foorder.service.UserService;
import org.springframework.samples.foorder.web.CamareroController;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = CamareroController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class CamareroControllerTests {

	private static final int TEST_CAMARERO_ID = 1;
	private static final String TEST_USER_PROPIETARIO = "prince";

	@MockBean
	private CamareroService camareroService;

	@MockBean
	private CamareroRepository camareroRepository;
	

	@MockBean
	private AuthoritiesService authoritiesService;
	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;
	private Camarero camarero;
	private User user;

	@BeforeEach
	void setup() {
		camarero = Mockito.mock(Camarero.class);
// falta funcion de user service		given(this.userService.findUser(TEST_USER_PROPIETARIO)).willReturn("propietario");
//		List listacam = new ArrayList<Camarero>();
		Camarero camarero = new Camarero();
		camarero.setApellido("ffffff");
		camarero.setContrasena("12345");
		camarero.setGmail("xxxxx@gmail.com");
		camarero.setName("aaaass");
		camarero.setTelefono("123456789");
		camarero.setUsuario("jose");
//		listacam.add(camarero);
//		Iterable<Camarero> cam =listacam;
		given(this.camareroService.findAll()).willReturn(new ArrayList<Camarero>());
		given(this.camareroService.findById(TEST_CAMARERO_ID)).willReturn(Optional.of(camarero));

	}

	@WithMockUser(value = "propietario")
	@Test
	@DisplayName("Listar Camarero")
	void showCamarerosList() throws Exception {
		mockMvc.perform(get("/camareros")).andExpect(status().isOk()).andExpect(view().name("camareros/listaCamareros"))
				.andExpect(model().attributeExists("camareros"));
	}


	@WithMockUser(value = "spring")
	@Test
	void processCreationCamareroSuccess() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/camareros/save").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "Pepe")
				.param("apellido", "esc")
				.param("telefono", "543972343")
				.param("gmail", "pe2000@gmail.com")
				.param("contrasena", "12345")
				.param("usuario", "jose")
				.with(csrf())).andExpect(model().attributeExists("camarero"))
	.andExpect(status().is2xxSuccessful()).andExpect(view().name("camareros/listaCamareros"));
		}
	

	@WithMockUser(value = "spring")
    @Test
	void processCreationCamareroHasErrors() throws Exception {
		mockMvc.perform(post("/camareros/save").with(csrf())
				.param("name", "")
				.param("apellidos", "")
				.param("telefono", "543972343")
				.param("gmail", "p2000")
				.param("usuario", "jose2")
				.param("contrasena", "12345"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("exception"));

	}

}
