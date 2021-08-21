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
import org.springframework.samples.foorder.model.TipoProducto;
import org.springframework.samples.foorder.service.TipoProductoService;
import org.springframework.samples.foorder.web.TipoProductoFormatter;

@ExtendWith(MockitoExtension.class)
public class TipoProductoFormatterTest {
	@Mock
	private TipoProductoService tipoProductoService;

	private TipoProductoFormatter tipoProductoFormatter;
	
	private Collection<TipoProducto> tipoProductos;
	
	@BeforeEach
	void setup() {
		
		tipoProductoFormatter= new TipoProductoFormatter(tipoProductoService);
		tipoProductos= new ArrayList<TipoProducto>();
		TipoProducto carne= new TipoProducto();
		carne.setName("carne");
		TipoProducto pescado= new TipoProducto();
		pescado.setName("pescado");
		tipoProductos.add(carne);
		tipoProductos.add(pescado);
		
	}
	
	@Test
	void testPrint() {
		TipoProducto p= new TipoProducto();
		p.setName("carne");
		String estado=tipoProductoFormatter.print(p, Locale.ENGLISH);
		assertEquals("carne", estado);
	}
	
	@Test
	void shouldParseEstadotipoProducto() throws ParseException {
		Mockito.when(tipoProductoService.findAll()).thenReturn(tipoProductos);
		TipoProducto carne= tipoProductoFormatter.parse("carne", Locale.ENGLISH);
		assertEquals("carne", carne.getName());
	}
	
	@Test
	void shouldThrowParseException() throws ParseException {
		Mockito.when(tipoProductoService.findAll()).thenReturn(tipoProductos);
		assertThrows(ParseException.class, () -> {
			tipoProductoFormatter.parse("canne", Locale.ENGLISH);
		});
	}

}
