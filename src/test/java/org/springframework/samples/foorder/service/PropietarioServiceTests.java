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
import org.springframework.samples.foorder.model.Propietario;
import org.springframework.samples.foorder.service.exceptions.DuplicatedPedidoException;
import org.springframework.stereotype.Service;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class PropietarioServiceTests {
    @Autowired
    private PropietarioService propietarioService;
    
    @Autowired
    private UserService userService;

    private Propietario prop;
    private Propietario prop2;
    private String usuario;

    @BeforeEach                                         
    public void setUp() throws Exception {
        usuario="usuarioTest";
        prop= new Propietario();
        prop.setId(1);
        prop.setName("Pedro");
        prop.setUsuario(usuario);  
        prop.setApellido("Avr");
        prop.setContrasena("12345");
        prop.setGmail("AS@gmail.com");
        prop.setTelefono("123456789");
        
        
        prop2 = new Propietario();
        prop2.setId(2);
        prop2.setName("Pedro");
        prop2.setUsuario(usuario);  
        prop2.setApellido("Avr");
        prop2.setContrasena("12345");
        prop2.setGmail("AS@gmail.com");
        prop2.setTelefono("123456789");
    }

    @Test
    @DisplayName("Borra propietario y su usuario correctamente")
    void shouldDeletepropietario() {
        Iterable<Propietario> it= propietarioService.findAll();
        List<Propietario> ls=Lists.newArrayList(it);
        int foundBefore = ls.size();

        Propietario cm = this.propietarioService.findById(1).get();
        this.propietarioService.deleteById(cm.getId());      

        List<Propietario> ls2=Lists.newArrayList(propietarioService.findAll());

        int foundAfter = ls2.size();
        assertThat(foundBefore).isEqualTo(foundAfter+1);
    }

    @Test
    @DisplayName("guarda propietario y su usuario correctamente")
    public void shouldInsertpropietario() throws DuplicatedPedidoException {
    	Propietario prop3 = new Propietario();
         prop3.setName("Pedro");
         prop3.setUsuario(usuario);  
         prop3.setApellido("Avr");
         prop3.setContrasena("12345");
         prop3.setGmail("AS@gmail.com");
         prop3.setTelefono("123456789");
        Iterable<Propietario> it= propietarioService.findAll();
        List<Propietario> ls=Lists.newArrayList(it);
        int before = ls.size();
        this.propietarioService.save(prop3);
        assertThat(prop.getId().longValue()).isNotEqualTo(0);
        int after = Lists.newArrayList(propietarioService.findAll()).size();
        assertThat(before+1).isEqualTo(after);
    }
    
    @Test
    @DisplayName("comprueba que un propietario actualizado borra su antiguo usuario")
    public void propietarioActualizadoBorraAntiguoUser() throws DuplicatedPedidoException {
        this.propietarioService.save(prop);
        Propietario prop2 = new Propietario(prop);
        prop2.setId(prop.getId());
        prop2.setUsuario("user2");
        this.propietarioService.save(prop2);
        assertThat(this.userService.findUser(usuario).equals(null));
    }

    
    @Test
    @DisplayName("encuentra propietario con mismo nombre de usuario")
    public void propietarioActualizadoConMismoUsuario() throws DuplicatedPedidoException {
        this.propietarioService.save(prop);
        Propietario prop2 = new Propietario(prop);
        prop2.setId(prop.getId());
        prop2.setUsuario("user2");
        assertThat(this.propietarioService.propietarioConMismoUsuario(prop2));
    }
    
	
	@Test
	void shouldNotCreatepropietarioUserSaveDuplicated() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.propietarioService.save(prop);
		Errors constraintViolations =  new BeanPropertyBindingResult(prop, "");
		FieldError error= this.propietarioService.resultUserSave(prop, (BindingResult) constraintViolations);
		((AbstractBindingResult) constraintViolations).addError(error);
		assertThat(constraintViolations.hasErrors()).isTrue();
	}
	
	@Test
	void shouldNotCreatepropietarioUserEditDuplicated() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.propietarioService.save(prop);
		Errors constraintViolations =  new BeanPropertyBindingResult(prop2, "");
		FieldError error= this.propietarioService.resultUserEdit(prop2, (BindingResult) constraintViolations);
		((AbstractBindingResult) constraintViolations).addError(error);
		assertThat(constraintViolations.hasErrors()).isTrue();
	}
	
	@Test
	void propietarioConMismoUsuarioTrue() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.propietarioService.save(prop);
		assertThat(this.propietarioService.propietarioConMismoUsuario(prop)).isTrue();
	}
	
	@Test
	void shouldNotCreatepropietarioUserSaveDuplicatedReturnNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		prop.setUsuario("josebas");
		prop2.setUsuario("josebas2");
		Errors constraintViolations =  new BeanPropertyBindingResult(prop, "");
		FieldError error= this.propietarioService.resultUserSave(prop2, (BindingResult) constraintViolations);
		assertThat(error).isNull();
	}
	
	@Test
	void shouldNotCreatepropietarioUserEditDuplicatedReturnNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		prop.setUsuario("josebas");
		prop2.setUsuario("josebas2");
		Errors constraintViolations =  new BeanPropertyBindingResult(prop, "");
		FieldError error= this.propietarioService.resultUserEdit(prop2, (BindingResult) constraintViolations);
		assertThat(error).isNull();
	}
	
	@Test
	void countWorksOK() {
		Integer nProp=this.propietarioService.count();
		assertThat(nProp.equals(2));
	}
    
}