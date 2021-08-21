package org.springframework.samples.foorder.formatters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.foorder.model.Plato;
import org.springframework.samples.foorder.service.PlatoService;
import org.springframework.samples.foorder.web.PlatoFormatter;

@ExtendWith(MockitoExtension.class)
public class PlatoFormatterTest {
	@Mock
	private PlatoService platoService;

	private PlatoFormatter platoFormatter;
	
	private Collection<Plato> platos;
	
	@BeforeEach
	void setup() {
		
		platoFormatter= new PlatoFormatter(platoService);
		platos= new ArrayList<Plato>();
		Plato albondigas= new Plato();
		albondigas.setName("albondigas");
		Plato habichuelasConPollo= new Plato();
		habichuelasConPollo.setName("habichuelasConPollo");
		platos.add(albondigas);
		platos.add(habichuelasConPollo);
		
	}
	
	@Test
	void testPrint() {
		Plato p= new Plato();
		p.setName("albondigas");
		String estado=platoFormatter.print(p, Locale.ENGLISH);
		assertEquals("albondigas", estado);
	}
	
	@Test
	void shouldParseEstadoPlato() throws ParseException {
		Mockito.when(platoService.findAll()).thenReturn(platos);
		Plato albondigas= platoFormatter.parse("albondigas", Locale.ENGLISH);
		assertEquals("albondigas", albondigas.getName());
	}
	
	@Test
	void shouldThrowParseException() throws ParseException {
		Mockito.when(platoService.findAll()).thenReturn(platos);
		assertThrows(ParseException.class, () -> {
			platoFormatter.parse("almondigas", Locale.ENGLISH);
		});
	}

}
