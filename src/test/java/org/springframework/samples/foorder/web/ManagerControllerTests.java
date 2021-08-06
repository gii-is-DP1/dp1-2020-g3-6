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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.foorder.configuration.SecurityConfiguration;
import org.springframework.samples.foorder.model.Manager;
import org.springframework.samples.foorder.repository.ManagerRepository;
import org.springframework.samples.foorder.service.AuthoritiesService;
import org.springframework.samples.foorder.service.ManagerService;
import org.springframework.samples.foorder.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = ManagerController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class ManagerControllerTests {

	private static final int TEST_manager_ID = 1;
	private static final int TEST_manager_FAKE_ID = -1;

	@MockBean
	private ManagerService managerService;

	@MockBean
	private ManagerRepository managerRepository;


	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setup() {
		Manager manager = new Manager();
		manager.setApellido("ffffff");
		manager.setContrasena("12345");
		manager.setGmail("xxxxx@gmail.com");
		manager.setName("aaaass");
		manager.setTelefono("123456789");
		manager.setUsuario("jose");
		given(this.managerService.findAll()).willReturn(new ArrayList<Manager>());
		given(this.managerService.findById(TEST_manager_ID)).willReturn(Optional.of(manager));

	}
	
	@WithMockUser(value = "propietario")
	@Test
	@DisplayName("Listar Manager")
	void showManagersList() throws Exception {
		mockMvc.perform(get("/managers")).andExpect(status().isOk()).andExpect(view().name("managers/listaManagers"))
		.andExpect(model().attributeExists("managers"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void processCreationCamareroSuccess() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/managers/save").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "Pepe")
				.param("apellido", "esc")
				.param("telefono", "543972343")
				.param("gmail", "pe2000@gmail.com")
				.param("contrasena", "12345abvM")
				.param("usuario", "jose")
				.with(csrf())).andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/managers?message=Guardado correctamente"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void processCreationCamareroHasErrors() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/managers/save").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "Pe")
				.param("apellido", "es")
				.param("telefono", "543972343")
				.param("gmail", "pe2000")
				.param("contrasena", "12345")
				.param("usuario", "jo")
				.with(csrf())).andExpect(status().isOk())
		.andExpect(model().attributeHasErrors("manager"))
		.andExpect(model().attributeHasFieldErrors("manager", "name"))
		.andExpect(model().attributeHasFieldErrors("manager", "apellido"))
		.andExpect(model().attributeHasFieldErrors("manager", "gmail"))
		.andExpect(model().attributeHasFieldErrors("manager", "usuario"))
		.andExpect(model().attributeHasFieldErrors("manager", "contrasena"))
		.andExpect(view().name("managers/editManager"));

	}
	
	@WithMockUser(value = "spring")
	@Test
	void testDeleteCamarero() throws Exception {
		mockMvc.perform(get("/managers/delete/{managerId}", TEST_manager_ID)
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/managers?message=Borrado Correctamente"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testDeleteFakeCamarero() throws Exception {
		mockMvc.perform(get("/managers/delete/{managerId}", TEST_manager_FAKE_ID)
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/managers?message=Manager no encontrado"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateCamareroForm() throws Exception {
		mockMvc.perform(get("/managers/edit/{managerId}", TEST_manager_ID)
				.with(csrf()))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("managers/editarManager"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateFakeCamareroForm() throws Exception {
		mockMvc.perform(get("/managers/edit/{managerId}", TEST_manager_FAKE_ID)
				.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/managers?message=Ese manager no existe"));
	}

	@WithMockUser(value = "spring")
	@Test
	void processEditCamareroSuccess() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/managers/edit").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "Pepe")
				.param("apellido", "esc")
				.param("telefono", "543972343")
				.param("gmail", "pe2000@gmail.com")
				.param("contrasena", "12345abvM")
				.param("usuario", "jose")
				.with(csrf())).andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/managers?message=Actualizado correctamente"));
	}


	@WithMockUser(value = "spring")
	@Test
	void processEditCamareroHasErrors() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/managers/edit").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "Pe")
				.param("apellido", "es")
				.param("telefono", "543972343")
				.param("gmail", "pe2000")
				.param("contrasena", "12345")
				.param("usuario", "jo")
				.with(csrf())).andExpect(status().isOk())
		.andExpect(model().attributeHasErrors("manager"))
		.andExpect(model().attributeHasFieldErrors("manager", "name"))
		.andExpect(model().attributeHasFieldErrors("manager", "apellido"))
		.andExpect(model().attributeHasFieldErrors("manager", "gmail"))
		.andExpect(model().attributeHasFieldErrors("manager", "usuario"))
		.andExpect(model().attributeHasFieldErrors("manager", "contrasena"))
		.andExpect(view().name("managers/editarManager"));

	}
	
}
