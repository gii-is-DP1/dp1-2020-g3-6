package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Camarero;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPedidoException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class CamareroServiceTests {
	@Autowired
	private CamareroService camareroService;
	
	@Autowired
	private UserService userService;

	private Camarero cam;

	private String usuario;

	@BeforeEach                                         
	public void setUp() throws Exception {
		usuario="usuarioTest";
		cam= new Camarero();
		cam.setName("Pedro");
		cam.setUsuario(usuario);  
		cam.setApellido("Avr");
		cam.setContrasena("12345");
		cam.setGmail("AS@gmail.com");
		cam.setTelefono("123456789");
	}

	@Test
	@DisplayName("Borra camarero y su usuario correctamente")
	void shouldDeleteCamarero() {
		Iterable<Camarero> it= camareroService.findAll();
		List<Camarero> ls=Lists.newArrayList(it);
		int foundBefore = ls.size();

		Camarero cm = this.camareroService.findById(1).get();
		this.camareroService.deleteById(cm.getId());	  

		List<Camarero> ls2=Lists.newArrayList(camareroService.findAll());

		int foundAfter = ls2.size();
		assertThat(foundBefore).isEqualTo(foundAfter+1);
	}

	@Test
	@DisplayName("guarda camarero y su usuario correctamente")
	public void shouldInsertCamarero() throws DuplicatedPedidoException {
		Iterable<Camarero> it= camareroService.findAll();
		List<Camarero> ls=Lists.newArrayList(it);
		int before = ls.size();
		this.camareroService.save(cam);
		assertThat(cam.getId().longValue()).isNotEqualTo(0);
		int after = Lists.newArrayList(camareroService.findAll()).size();
		assertThat(before+1).isEqualTo(after);
	}
	
	@Test
	@DisplayName("comprueba que un camarero actualizado borra su antiguo usuario")
	public void CamareroActualizadoBorraAntiguoUser() throws DuplicatedPedidoException {
		this.camareroService.save(cam);
		Camarero cam2 = new Camarero(cam);
		cam2.setId(cam.getId());
		cam2.setUsuario("user2");
		this.camareroService.save(cam2);
		assertThat(this.userService.findUser(usuario).equals(null));
	}

	@Test
	@DisplayName("encuentra camarero por usuario correctamente")
	public void shouldfindCamareroByUser() throws DuplicatedPedidoException {
		Camarero c=this.camareroService.findByUser(usuario);
		assertThat(c==cam);
	}
	
	@Test
	@DisplayName("encuentra camarero con mismo nombre de usuario")
	public void camareroActualizadoConMismoUsuario() throws DuplicatedPedidoException {
		this.camareroService.save(cam);
		Camarero cam2 = new Camarero(cam);
		cam2.setId(cam.getId());
		cam2.setUsuario("user2");
		assertThat(this.camareroService.CamareroConMismoUsuario(cam2));
	}
	
	@Test
	@DisplayName("borraFieldErrorsCorrectamente")
	public void borraFieldErrorsCorrectamente() throws DuplicatedPedidoException {
		BindingResult intento= new BeanPropertyBindingResult(cam, "camarero");
		intento.rejectValue("usuario", "prueba");
		intento.rejectValue("name", "prueba2");
		assertThat(this.camareroService.ErroresSinMismoUser(cam, intento).getFieldErrors().size()<2);
		
	}
}
