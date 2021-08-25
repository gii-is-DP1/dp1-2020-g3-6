package org.springframework.samples.foorder.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.foorder.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class AuthoritiesServiceTest {
    @Autowired
	private AuthoritiesService authoritiesService;
	
    @Test
	void shouldCrearUsuarioSuccess() {
		String username="pedro";
		String contrasena="ABC1234XYZ";
		User user=this.authoritiesService.crearUsuario(username, contrasena);
		assertEquals(user.getUsername(), username);
	
	}
    
    @Test
   	void shouldSaveAuthorities() {
   		String username="carlos";
   		String role="camarero";
   		Assertions.assertDoesNotThrow(() -> {
   			this.authoritiesService.saveAuthorities(username, role);
		});
   	}
    
    @Test
   	void shouldNotSaveAuthorities() {
   		String username="usuarioNoExistente";
   		String role="camarero";
   		assertThrows(DataAccessException.class, () -> {
   			this.authoritiesService.saveAuthorities(username, role);
		},"User '"+username+"' not found!");
   	}
    
    @Test
   	void shouldFindAllUsernames() {
    	List<String> lista=this.authoritiesService.findAllUsernames();
    	assertEquals(lista.size(), 8);
   	}
}
