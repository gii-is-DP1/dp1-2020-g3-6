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
import org.springframework.samples.foorder.model.Manager;
import org.springframework.samples.foorder.service.exceptions.DuplicatedPedidoException;
import org.springframework.stereotype.Service;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class ManagerServiceTests {
    @Autowired
    private ManagerService managerService;
    @Autowired
    private UserService userService;

    private Manager man;
    private Manager man2;
    private String usuario;

    @BeforeEach                                         
    public void setUp() throws Exception {
        usuario="usuarioTest";
        man= new  Manager();
        man.setId(1);
        man.setName("Pedro");
        man.setUsuario(usuario);  
        man.setApellido("Avr");
        man.setContrasena("12345");
        man.setGmail("AS@gmail.com");
        man.setTelefono("123456789");
        
        
        man2 = new  Manager();
        man2.setId(2);
        man2.setName("Pedro");
        man2.setUsuario(usuario);  
        man2.setApellido("Avr");
        man2.setContrasena("12345");
        man2.setGmail("AS@gmail.com");
        man2.setTelefono("123456789");
    }

    @Test
    @DisplayName("Borra manager y su usuario correctamente")
    void shouldDeletemanager() {
        Iterable< Manager> it= managerService.findAll();
        List< Manager> ls=Lists.newArrayList(it);
        int foundBefore = ls.size();

        Manager cm = this.managerService.findById(1).get();
        this.managerService.deleteById(cm.getId());      

        List<Manager> ls2=Lists.newArrayList(managerService.findAll());

        int foundAfter = ls2.size();
        assertThat(foundBefore).isEqualTo(foundAfter+1);
    }

    @Test
    @DisplayName("guarda manager y su usuario correctamente")
    public void shouldInsertmanager() throws DuplicatedPedidoException {
    	 Manager man3 = new Manager();
         man3.setName("Pedro");
         man3.setUsuario(usuario);  
         man3.setApellido("Avr");
         man3.setContrasena("12345");
         man3.setGmail("AS@gmail.com");
         man3.setTelefono("123456789");
        Iterable<Manager> it= managerService.findAll();
        List<Manager> ls=Lists.newArrayList(it);
        int before = ls.size();
        this.managerService.save(man3);
        assertThat(man.getId().longValue()).isNotEqualTo(0);
        int after = Lists.newArrayList(managerService.findAll()).size();
        assertThat(before+1).isEqualTo(after);
    }
    
    @Test
    @DisplayName("comprueba que un manager actualizado borra su antiguo usuario")
    public void managerActualizadoBorraAntiguoUser() throws DuplicatedPedidoException {
        this.managerService.save(man);
        Manager man2 = new Manager(man);
        man2.setId(man.getId());
        man2.setUsuario("user2");
        this.managerService.save(man2);
        assertThat(this.userService.findUser(usuario).equals(null));
    }
    
    @Test
    @DisplayName("encuentra manager con mismo nombre de usuario")
    public void managerActualizadoConMismoUsuario() throws DuplicatedPedidoException {
        this.managerService.save(man);
        Manager man2 = new Manager(man);
        man2.setId(man.getId());
        man2.setUsuario("user2");
        assertThat(this.managerService.managerConMismoUsuario(man2));
    }
    
	
	@Test
	void shouldNotCreatemanagerUserSaveDuplicated() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.managerService.save(man);
		Errors constraintViolations =  new BeanPropertyBindingResult(man, "");
		FieldError error= this.managerService.resultUserSave(man, (BindingResult) constraintViolations);
		((AbstractBindingResult) constraintViolations).addError(error);
		assertThat(constraintViolations.hasErrors()).isTrue();
	}
	
	@Test
	void shouldNotCreatemanagerUserEditDuplicated() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.managerService.save(man);
	
		Errors constraintViolations =  new BeanPropertyBindingResult(man2, "");
		FieldError error= this.managerService.resultUserEdit(man2, (BindingResult) constraintViolations);
		((AbstractBindingResult) constraintViolations).addError(error);
		assertThat(constraintViolations.hasErrors()).isTrue();
	}
	
	@Test
	void managerConMismoUsuarioTrue() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.managerService.save(man);
		assertThat(this.managerService.managerConMismoUsuario(man)).isTrue();
	}
	
	@Test
	void shouldNotCreatemanagerUserSaveDuplicatedReturnNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		man.setUsuario("josebas");
		man2.setUsuario("josebas2");
		Errors constraintViolations =  new BeanPropertyBindingResult(man, "");
		FieldError error= this.managerService.resultUserSave(man2, (BindingResult) constraintViolations);
		assertThat(error).isNull();
	}
	
	@Test
	void shouldNotCreatemanagerUserEditDuplicatedReturnNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		man.setUsuario("josebas");
		man2.setUsuario("josebas2");
		Errors constraintViolations =  new BeanPropertyBindingResult(man, "");
		FieldError error= this.managerService.resultUserEdit(man2, (BindingResult) constraintViolations);
		assertThat(error).isNull();
	}
    
}