package org.springframework.samples.foorder.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Locale;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.foorder.model.Camarero;
import org.springframework.samples.foorder.service.exceptions.DuplicatedPedidoException;
import org.springframework.stereotype.Service;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class CamareroServiceTests {
    @Autowired
    private CamareroService camareroService;
    
    @Autowired
    private UserService userService;

    private Camarero cam;
    private Camarero cam2;
    private String usuario;

    @BeforeEach                                         
    public void setUp() throws Exception {
        usuario="usuarioTest";
        cam= new Camarero();
        cam.setId(1);
        cam.setName("Pedro");
        cam.setUsuario(usuario);  
        cam.setApellido("Avr");
        cam.setContrasena("12345");
        cam.setGmail("AS@gmail.com");
        cam.setTelefono("123456789");
        
        
        cam2 = new Camarero();
        cam2.setId(2);
        cam2.setName("Pedro");
        cam2.setUsuario(usuario);  
        cam2.setApellido("Avr");
        cam2.setContrasena("12345");
        cam2.setGmail("AS@gmail.com");
        cam2.setTelefono("123456789");
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
    	 Camarero cam3 = new Camarero();
         cam3.setName("Pedro");
         cam3.setUsuario(usuario);  
         cam3.setApellido("Avr");
         cam3.setContrasena("12345");
         cam3.setGmail("AS@gmail.com");
         cam3.setTelefono("123456789");
        Iterable<Camarero> it= camareroService.findAll();
        List<Camarero> ls=Lists.newArrayList(it);
        int before = ls.size();
        this.camareroService.save(cam3);
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
	void shouldNotCreateCamareroUserSaveDuplicated() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.camareroService.save(cam);
		Errors constraintViolations =  new BeanPropertyBindingResult(cam, "");
		FieldError error= this.camareroService.resultUserSave(cam, (BindingResult) constraintViolations);
		((AbstractBindingResult) constraintViolations).addError(error);
		assertThat(constraintViolations.hasErrors()).isTrue();
	}
	
	@Test
	void shouldNotCreateCamareroUserEditDuplicated() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.camareroService.save(cam);
	
		Errors constraintViolations =  new BeanPropertyBindingResult(cam2, "");
		FieldError error= this.camareroService.resultUserEdit(cam2, (BindingResult) constraintViolations);
		((AbstractBindingResult) constraintViolations).addError(error);
		assertThat(constraintViolations.hasErrors()).isTrue();
	}
	
	@Test
	void CamareroConMismoUsuarioTrue() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.camareroService.save(cam);
		assertThat(this.camareroService.CamareroConMismoUsuario(cam)).isTrue();
	}
	
	@Test
	void shouldNotCreateCamareroUserSaveDuplicatedReturnNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		cam.setUsuario("josebas");
		cam2.setUsuario("josebas2");
		Errors constraintViolations =  new BeanPropertyBindingResult(cam, "");
		FieldError error= this.camareroService.resultUserSave(cam2, (BindingResult) constraintViolations);
		assertThat(error).isNull();
	}
	
	@Test
	void shouldNotCreateCamareroUserEditDuplicatedReturnNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		cam.setUsuario("josebas");
		cam2.setUsuario("josebas2");
		Errors constraintViolations =  new BeanPropertyBindingResult(cam, "");
		FieldError error= this.camareroService.resultUserEdit(cam2, (BindingResult) constraintViolations);
		assertThat(error).isNull();
	}
    
}