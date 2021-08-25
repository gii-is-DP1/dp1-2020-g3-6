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
import org.springframework.samples.foorder.model.EstadoPlato;
import org.springframework.samples.foorder.service.EstadoPlatoService;
import org.springframework.samples.foorder.web.EstadoPlatoFormatter;


@ExtendWith(MockitoExtension.class)
class EstadoPlatoFormatterTest {
	
	@Mock
	private EstadoPlatoService estadoPlatoService;

	private EstadoPlatoFormatter estadoPlatoFormatter;
	
	private Collection<EstadoPlato> estados;
	
	@BeforeEach
	void setup() {
		
		estadoPlatoFormatter= new EstadoPlatoFormatter(estadoPlatoService);
		estados= new ArrayList<EstadoPlato>();
		EstadoPlato ENCOLA= new EstadoPlato();
		ENCOLA.setName("ENCOLA");
		EstadoPlato ENPROCESO= new EstadoPlato();
		ENPROCESO.setName("ENPROCESO");
		estados.add(ENCOLA);
		estados.add(ENPROCESO);
		
	}
	
	@Test
	void testPrint() {
		EstadoPlato ep= new EstadoPlato();
		ep.setName("ENCOLA");
		String estado=estadoPlatoFormatter.print(ep, Locale.ENGLISH);
		assertEquals("ENCOLA", estado);
	}
	
	@Test
	void shouldParseEstadoPlato() throws ParseException {
		Mockito.when(estadoPlatoService.findAll()).thenReturn(estados);
		EstadoPlato ENCOLA= estadoPlatoFormatter.parse("ENCOLA", Locale.ENGLISH);
		assertEquals("ENCOLA", ENCOLA.getName());
	}
	
	@Test
	void shouldThrowParseException() throws ParseException {
		Mockito.when(estadoPlatoService.findAll()).thenReturn(estados);
		assertThrows(ParseException.class, () -> {
			estadoPlatoFormatter.parse("INEXISTENTE", Locale.ENGLISH);
		});
	}

}
