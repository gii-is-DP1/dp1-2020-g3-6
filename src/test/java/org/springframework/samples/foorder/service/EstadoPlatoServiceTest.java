package org.springframework.samples.foorder.service;
import static org.junit.Assert.assertEquals;
import java.util.Collection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.foorder.model.EstadoPlato;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class EstadoPlatoServiceTest {

    @Autowired
    protected EstadoPlatoService estadoPlatoService;

    @Test
    public void testEncontrarProductos() {
        Collection<EstadoPlato> count = (Collection<EstadoPlato>) estadoPlatoService.findAll();
        assertEquals(count.size(),3);
    }
}