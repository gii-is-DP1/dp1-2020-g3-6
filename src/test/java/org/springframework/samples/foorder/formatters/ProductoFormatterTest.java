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
import org.springframework.samples.foorder.model.Producto;
import org.springframework.samples.foorder.service.ProductoService;
import org.springframework.samples.foorder.web.ProductoFormatter;


@ExtendWith(MockitoExtension.class)
class ProductoFormatterTest {
	
	@Mock
	private ProductoService productoService;

	private ProductoFormatter productoFormatter;
	
	private Collection<Producto> productos;
	
	@BeforeEach
	void setup() {
		
		productoFormatter= new ProductoFormatter(productoService);
		productos= new ArrayList<Producto>();
		Producto pan= new Producto();
		pan.setName("pan");
		Producto solomillo= new Producto();
		solomillo.setName("solomillo");
		productos.add(pan);
		productos.add(solomillo);
		
	}
	
	@Test
	void testPrint() {
		Producto ep= new Producto();
		ep.setName("pan");
		String estado=productoFormatter.print(ep, Locale.ENGLISH);
		assertEquals("pan", estado);
	}
	
	@Test
	void shouldParseproducto() throws ParseException {
		Mockito.when(productoService.findAll()).thenReturn(productos);
		Producto pan= productoFormatter.parse("pan", Locale.ENGLISH);
		assertEquals("pan", pan.getName());
	}
	
	@Test
	void shouldThrowParseException() throws ParseException {
		Mockito.when(productoService.findAll()).thenReturn(productos);
		assertThrows(ParseException.class, () -> {
			productoFormatter.parse("soloquillo", Locale.ENGLISH);
		});
	}

}
