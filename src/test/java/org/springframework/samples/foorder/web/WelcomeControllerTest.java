package org.springframework.samples.foorder.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.foorder.configuration.SecurityConfiguration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers=WelcomeController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class WelcomeControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@WithMockUser(value = "propietario")
	@Test
	@DisplayName("test welcome controller")
	void showWelcome() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view()
		.name("welcome"));
	}
	
	@WithMockUser(value = "propietario")
	@Test
	@DisplayName("test login")
	void showLogin() throws Exception {
		mockMvc.perform(get("/login")).andExpect(status().isOk())
		.andExpect(status().is2xxSuccessful());
	}
	
	@WithMockUser(value = "propietario")
	@Test
	@DisplayName("test login")
	void showHome() throws Exception {
		mockMvc.perform(get("/home")).andExpect(status().isOk())
		.andExpect(status().is2xxSuccessful());
	}

}
