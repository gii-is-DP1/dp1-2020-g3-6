
package org.springframework.samples.foorder.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.foorder.configuration.SecurityConfiguration;
import org.springframework.samples.foorder.model.Camarero;
import org.springframework.samples.foorder.repository.CamareroRepository;
import org.springframework.samples.foorder.service.AuthoritiesService;
import org.springframework.samples.foorder.service.CamareroService;
import org.springframework.samples.foorder.service.UserService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = CamareroController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class CamareroControllerTests {

	private static final int TEST_CAMARERO_ID = 1;
	private static final int TEST_CAMARERO_FAKE_ID = -1;

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

	@BeforeEach
	void setup() {
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
				.param("contrasena", "12345abvM")
				.param("usuario", "jose")
				.with(csrf())).andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/camareros?message=Guardado correctamente"));
	}


	@WithMockUser(value = "spring")
	@Test
	void processCreationCamareroHasErrors() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/camareros/save").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "Pe")
				.param("apellido", "es")
				.param("telefono", "543972343")
				.param("gmail", "pe2000")
				.param("contrasena", "12345")
				.param("usuario", "jo")
				.with(csrf())).andExpect(status().isOk())
		.andExpect(model().attributeHasErrors("camarero"))
		.andExpect(model().attributeHasFieldErrors("camarero", "name"))
		.andExpect(model().attributeHasFieldErrors("camarero", "apellido"))
		.andExpect(model().attributeHasFieldErrors("camarero", "gmail"))
		.andExpect(model().attributeHasFieldErrors("camarero", "usuario"))
		.andExpect(model().attributeHasFieldErrors("camarero", "contrasena"))
		.andExpect(view().name("camareros/editCamarero"));

	}

	@WithMockUser(value = "spring")
	@Test
	void testDeleteCamarero() throws Exception {
		mockMvc.perform(get("/camareros/delete/{camareroId}", TEST_CAMARERO_ID)
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/camareros?message=Borrado correctamente"));
	}


	@WithMockUser(value = "spring")
	@Test
	void testDeleteFakeCamarero() throws Exception {
		mockMvc.perform(get("/camareros/delete/{camareroId}", TEST_CAMARERO_FAKE_ID)
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/camareros?message=Camarero no encontrado"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateCamareroForm() throws Exception {
		mockMvc.perform(get("/camareros/edit/{camareroId}", TEST_CAMARERO_ID)
				.with(csrf()))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("camareros/editarCamareros"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateFakeCamareroForm() throws Exception {
		mockMvc.perform(get("/camareros/edit/{camareroId}", TEST_CAMARERO_FAKE_ID)
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/camareros?message=Ese camarero no existe"));
	}

	@WithMockUser(value = "spring")
	@Test
	void processEditCamareroSuccess() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/camareros/edit").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "Pepe")
				.param("apellido", "esc")
				.param("telefono", "543972343")
				.param("gmail", "pe2000@gmail.com")
				.param("contrasena", "12345abvM")
				.param("usuario", "jose")
				.with(csrf())).andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/camareros?message=Actualizado correctamente"));
	}


	@WithMockUser(value = "spring")
	@Test
	void processEditCamareroHasErrors() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/camareros/edit").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "Pe")
				.param("apellido", "es")
				.param("telefono", "543972343")
				.param("gmail", "pe2000")
				.param("contrasena", "12345")
				.param("usuario", "jo")
				.with(csrf())).andExpect(status().isOk())
		.andExpect(model().attributeHasErrors("camarero"))
		.andExpect(model().attributeHasFieldErrors("camarero", "name"))
		.andExpect(model().attributeHasFieldErrors("camarero", "apellido"))
		.andExpect(model().attributeHasFieldErrors("camarero", "gmail"))
		.andExpect(model().attributeHasFieldErrors("camarero", "usuario"))
		.andExpect(model().attributeHasFieldErrors("camarero", "contrasena"))
		.andExpect(view().name("camareros/editarCamareros"));

	}
}
