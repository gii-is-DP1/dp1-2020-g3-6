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
import org.springframework.samples.foorder.model.Cocinero;
import org.springframework.samples.foorder.service.exceptions.DuplicatedPedidoException;
import org.springframework.stereotype.Service;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class CocineroServiceTest {
    @Autowired
    private CocineroService cocineroService;
    
    @Autowired
    private UserService userService;

    private Cocinero coc;
    private Cocinero coc2;
    private String usuario;

    @BeforeEach                                         
    public void setUp() throws Exception {
        usuario="usuarioTest";
        coc= new Cocinero();
        coc.setId(1);
        coc.setName("Pedro");
        coc.setUsuario(usuario);  
        coc.setApellido("Avr");
        coc.setContrasena("12345");
        coc.setGmail("AS@gmail.com");
        coc.setTelefono("123456789");
        
        
        coc2 = new Cocinero();
        coc2.setId(2);
        coc2.setName("Pedro");
        coc2.setUsuario(usuario);  
        coc2.setApellido("Avr");
        coc2.setContrasena("12345");
        coc2.setGmail("AS@gmail.com");
        coc2.setTelefono("123456789");
    }

    @Test
    @DisplayName("Borra cocinero y su usuario correctamente")
    void shouldDeletecocinero() {
        Iterable<Cocinero> it= cocineroService.findAll();
        List<Cocinero> ls=Lists.newArrayList(it);
        int foundBefore = ls.size();

        Cocinero cm = this.cocineroService.findById(1).get();
        this.cocineroService.deleteById(cm.getId());      

        List<Cocinero> ls2=Lists.newArrayList(cocineroService.findAll());

        int foundAfter = ls2.size();
        assertThat(foundBefore).isEqualTo(foundAfter+1);
    }

    @Test
    @DisplayName("guarda cocinero y su usuario correctamente")
    public void shouldInsertcocinero() throws DuplicatedPedidoException {
    	Cocinero coc3 = new Cocinero();
         coc3.setName("Pedro");
         coc3.setUsuario(usuario);  
         coc3.setApellido("Avr");
         coc3.setContrasena("12345");
         coc3.setGmail("AS@gmail.com");
         coc3.setTelefono("123456789");
        Iterable<Cocinero> it= cocineroService.findAll();
        List<Cocinero> ls=Lists.newArrayList(it);
        int before = ls.size();
        this.cocineroService.save(coc3);
        assertThat(coc.getId().longValue()).isNotEqualTo(0);
        int after = Lists.newArrayList(cocineroService.findAll()).size();
        assertThat(before+1).isEqualTo(after);
    }
    
    @Test
    @DisplayName("comprueba que un cocinero actualizado borra su antiguo usuario")
    public void cocineroActualizadoBorraAntiguoUser() throws DuplicatedPedidoException {
        this.cocineroService.save(coc);
        Cocinero coc2 = new Cocinero(coc);
        coc2.setId(coc.getId());
        coc2.setUsuario("user2");
        this.cocineroService.save(coc2);
        assertThat(this.userService.findUser(usuario).equals(null));
    }
    
    @Test
    @DisplayName("encuentra cocinero con mismo nombre de usuario")
    public void cocineroActualizadoConMismoUsuario() throws DuplicatedPedidoException {
        this.cocineroService.save(coc);
        Cocinero coc2 = new Cocinero(coc);
        coc2.setId(coc.getId());
        coc2.setUsuario("user2");
        assertThat(this.cocineroService.cocineroConMismoUsuario(coc2));
    }
    
	
	@Test
	void shouldNotCreatecocineroUserSaveDuplicated() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.cocineroService.save(coc);
		Errors constraintViolations =  new BeanPropertyBindingResult(coc, "");
		FieldError error= this.cocineroService.resultUserSave(coc, (BindingResult) constraintViolations);
		((AbstractBindingResult) constraintViolations).addError(error);
		assertThat(constraintViolations.hasErrors()).isTrue();
	}
	
	@Test
	void shouldNotCreatecocineroUserEditDuplicated() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.cocineroService.save(coc);
	
		Errors constraintViolations =  new BeanPropertyBindingResult(coc2, "");
		FieldError error= this.cocineroService.resultUserEdit(coc2, (BindingResult) constraintViolations);
		((AbstractBindingResult) constraintViolations).addError(error);
		assertThat(constraintViolations.hasErrors()).isTrue();
	}
	
	@Test
	void cocineroConMismoUsuarioTrue() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.cocineroService.save(coc);
		assertThat(this.cocineroService.cocineroConMismoUsuario(coc)).isTrue();
	}
	
	@Test
	void shouldNotCreatecocineroUserSaveDuplicatedReturnNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		coc.setUsuario("josebas");
		coc2.setUsuario("josebas2");
		Errors constraintViolations =  new BeanPropertyBindingResult(coc, "");
		FieldError error= this.cocineroService.resultUserSave(coc2, (BindingResult) constraintViolations);
		assertThat(error).isNull();
	}
	
	@Test
	void shouldNotCreatecocineroUserEditDuplicatedReturnNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		coc.setUsuario("josebas");
		coc2.setUsuario("josebas2");
		Errors constraintViolations =  new BeanPropertyBindingResult(coc, "");
		FieldError error= this.cocineroService.resultUserEdit(coc2, (BindingResult) constraintViolations);
		assertThat(error).isNull();
	}
    
}