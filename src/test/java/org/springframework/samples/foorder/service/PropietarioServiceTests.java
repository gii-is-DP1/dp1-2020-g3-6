package org.springframework.samples.foorder.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.foorder.model.Cocinero;
import org.springframework.samples.foorder.model.Propietario;
import org.springframework.samples.foorder.repository.PropietarioRepository;
import org.springframework.samples.foorder.service.PropietarioService;
import org.springframework.samples.foorder.service.exceptions.DuplicatedPedidoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import static org.assertj.core.api.Assertions.assertThat;



@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class PropietarioServiceTests {
    @Autowired
    private PropietarioService propietarioService;
    
    @Autowired
    private UserService userService;

    private Propietario propi;

    private String usuario;

    @BeforeEach                                         
    public void setUp() throws Exception {
        usuario="usuarioTestPropietario";
        propi= new Propietario();
        propi.setName("propi");
        propi.setUsuario(usuario);  
        propi.setApellido("presle");
        propi.setContrasena("12345");
        propi.setGmail("ASIES@gmail.com");
        propi.setTelefono("123456789");
    }

    @Test
    @DisplayName("Borra Propietario y su usuario correctamente")
    void shouldDeletePropietario() {
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
    @DisplayName("guarda Propietario y su usuario correctamente")
    public void shouldInsertPropietario() throws DuplicatedPedidoException {
        Iterable<Propietario> it= propietarioService.findAll();
        List<Propietario> ls=Lists.newArrayList(it);
        int before = ls.size();
        this.propietarioService.save(propi);
        assertThat(propi.getId().longValue()).isNotEqualTo(0);
        int after = Lists.newArrayList(propietarioService.findAll()).size();
        assertThat(before+1).isEqualTo(after);
    }
    
    @Test
    @DisplayName("comprueba que un propietario actualizado borra su antiguo usuario")
    public void PropietarioActualizadoBorraAntiguoUser() throws DuplicatedPedidoException {
        this.propietarioService.save(propi);
        Propietario propi2 = new Propietario(propi);
        propi2.setId(propi.getId());
        propi2.setUsuario("user2");
        this.propietarioService.save(propi2);
        assertThat(this.userService.findUser(usuario).equals(null));
    }

    @Test
    @DisplayName("encuentra propietario por id correctamente")
    public void shouldfindPropietarioById() throws DuplicatedPedidoException {
    	this.propietarioService.save(propi);
    	Propietario P=this.propietarioService.findById(propi.getId()).get();
        assertThat(P==propi);
    }
    
    @Test
    @DisplayName("encuentra Propietario con mismo nombre de usuario")
    public void PropietarioActualizadoConMismoUsuario() throws DuplicatedPedidoException {
        this.propietarioService.save(propi);
        Propietario propi2 = new Propietario(propi);
        propi2.setId(propi.getId());
        propi2.setUsuario("user2");
        assertThat(this.propietarioService.propietarioConMismoUsuario(propi2));
    }
    
    
}
