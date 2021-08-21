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
import org.springframework.samples.foorder.model.Proveedor;
import org.springframework.samples.foorder.service.ProveedorService;
import org.springframework.samples.foorder.web.ProveedorFormatter;


@ExtendWith(MockitoExtension.class)
class ProveedorFormatterTest {
	
	@Mock
	private ProveedorService proveedorService;

	private ProveedorFormatter proveedorFormatter;
	
	private Collection<Proveedor> estados;
	
	@BeforeEach
	void setup() {
		
		proveedorFormatter= new ProveedorFormatter(proveedorService);
		estados= new ArrayList<Proveedor>();
		Proveedor proveedor1= new Proveedor();
		proveedor1.setName("proveedor1");
		Proveedor proveedor2= new Proveedor();
		proveedor2.setName("proveedor2");
		estados.add(proveedor1);
		estados.add(proveedor2);
		
	}
	
	@Test
	void testPrint() {
		Proveedor ep= new Proveedor();
		ep.setName("proveedor1");
		String estado=proveedorFormatter.print(ep, Locale.ENGLISH);
		assertEquals("proveedor1", estado);
	}
	
	@Test
	void shouldParseproveedor() throws ParseException {
		Mockito.when(proveedorService.findAll()).thenReturn(estados);
		Proveedor proveedor1= proveedorFormatter.parse("proveedor1", Locale.ENGLISH);
		assertEquals("proveedor1", proveedor1.getName());
	}
	
	@Test
	void shouldThrowParseException() throws ParseException {
		Mockito.when(proveedorService.findAll()).thenReturn(estados);
		assertThrows(ParseException.class, () -> {
			proveedorFormatter.parse("proveedro", Locale.ENGLISH);
		});
	}

}
