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


import org.springframework.samples.foorder.model.Manager;
import org.springframework.samples.foorder.service.exceptions.DuplicatedPedidoException;
import org.springframework.stereotype.Service;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class ManagerServiceTests {
    @Autowired
    private ManagerService managerService;
    
    @Autowired
    private UserService userService;

    private Manager manager;

    private String usuario;

    @BeforeEach                                         
    public void setUp() throws Exception {
        usuario="usuarioTest";
        manager= new Manager();
        manager.setName("Pedro");
        manager.setUsuario(usuario);  
        manager.setApellido("Avr");
        manager.setContrasena("12345");
        manager.setGmail("AS@gmail.com");
        manager.setTelefono("123456789");
    }

    @Test
    @DisplayName("Borra manager y su usuario correctamente")
    void shouldDeleteManager() {
        Iterable<Manager> it= managerService.findAll();
        List<Manager> ls=Lists.newArrayList(it);
        int foundBefore = ls.size();

        Manager cm = this.managerService.findById(1).get();     
        this.managerService.deleteById(cm.getId());      

        List<Manager> ls2=Lists.newArrayList(managerService.findAll());

        int foundAfter = ls2.size();
        assertThat(foundBefore).isEqualTo(foundAfter+1);
    }

    @Test
    @DisplayName("guarda manager y su usuario correctamente")
    public void shouldInsertManager() throws DuplicatedPedidoException {
        Iterable<Manager> it= managerService.findAll();
        List<Manager> ls=Lists.newArrayList(it);
        int before = ls.size();
        this.managerService.save(manager);
        assertThat(manager.getId().longValue()).isNotEqualTo(0);
        int after = Lists.newArrayList(managerService.findAll()).size();
        assertThat(before+1).isEqualTo(after);
    }
    
    @Test
    @DisplayName("comprueba que un manager actualizado borra su antiguo usuario")
    public void managerActualizadoBorraAntiguoUser() throws DuplicatedPedidoException {
        this.managerService.save(manager);
        Manager manager2 = new Manager(manager);
        manager2.setId(manager.getId());
        manager2.setUsuario("user2");
        this.managerService.save(manager2);
        assertThat(this.userService.findUser(usuario).equals(null));
    }

    @Test
    @DisplayName("encuentra manager por id correctamente")
    public void shouldfindManagerById() throws DuplicatedPedidoException {
    	this.managerService.save(manager);
    	Manager c=this.managerService.findById(manager.getId()).get();
        assertThat(c==manager);
    }
    
    @Test
    @DisplayName("encuentra manager con mismo nombre de usuario")
    public void managerActualizadoConMismoUsuario() throws DuplicatedPedidoException {
        this.managerService.save(manager);
        Manager manager2 = new Manager(manager);
        manager2.setId(manager.getId());
        manager2.setUsuario("user2");
        assertThat(this.managerService.managerConMismoUsuario(manager2));
    }
    
    
}