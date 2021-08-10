package org.springframework.samples.foorder.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.samples.foorder.model.Cocinero;

import org.springframework.samples.foorder.service.exceptions.DuplicatedPedidoException;
import org.springframework.stereotype.Service;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class CocineroServiceTest {
    @Autowired
    private CocineroService cocineroService;
    
    @Autowired
    private UserService userService;

    private Cocinero coci;

    private String usuario;

    @BeforeEach                                         
    public void setUp() throws Exception {
        usuario="usuarioTestCocinero";
        coci= new Cocinero();
        coci.setName("Cain");
        coci.setUsuario(usuario);  
        coci.setApellido("Abel");
        coci.setContrasena("12345");
        coci.setGmail("ASIES@gmail.com");
        coci.setTelefono("123456789");
    }

    @Test
    @DisplayName("Borra cocinero y su usuario correctamente")
    void shouldDeleteCocinero() {
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
    public void shouldInsertCocinero() throws DuplicatedPedidoException {
        Iterable<Cocinero> it= cocineroService.findAll();
        List<Cocinero> ls=Lists.newArrayList(it);
        int before = ls.size();
        this.cocineroService.save(coci);
        assertThat(coci.getId().longValue()).isNotEqualTo(0);
        int after = Lists.newArrayList(cocineroService.findAll()).size();
        assertThat(before+1).isEqualTo(after);
    }
    
    @Test
    @DisplayName("comprueba que un cocinero actualizado borra su antiguo usuario")
    public void CocineroActualizadoBorraAntiguoUser() throws DuplicatedPedidoException {
        this.cocineroService.save(coci);
        Cocinero coci2 = new Cocinero(coci);
        coci2.setId(coci.getId());
        coci2.setUsuario("user2");
        this.cocineroService.save(coci2);
        assertThat(this.userService.findUser(usuario).equals(null));
    }

    @Test
    @DisplayName("encuentra cocinero por id correctamente")
    public void shouldfindCocineroById() throws DuplicatedPedidoException {
    	this.cocineroService.save(coci);
        Cocinero c=this.cocineroService.findById(coci.getId()).get();
        assertThat(c==coci);
    }
    
    @Test
    @DisplayName("encuentra Cocinero con mismo nombre de usuario")
    public void cocineroActualizadoConMismoUsuario() throws DuplicatedPedidoException {
        this.cocineroService.save(coci);
        Cocinero coci2 = new Cocinero(coci);
        coci2.setId(coci.getId());
        coci2.setUsuario("user2");
        assertThat(this.cocineroService.cocineroConMismoUsuario(coci2));
    }
    
   
}