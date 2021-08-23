package org.springframework.samples.foorder.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.foorder.model.EstadoPlato;
import org.springframework.samples.foorder.model.Plato;
import org.springframework.samples.foorder.model.PlatoPedido;
import org.springframework.samples.foorder.model.PlatoPedidoDTO;
import org.springframework.samples.foorder.web.PlatoPedidoConverter;

@ExtendWith(MockitoExtension.class)
public class PlatoPedidoConverterTest {

	@Mock
	private PlatoPedidoConverter platoPedidoConverter;
	
	private PlatoPedido platoPedido;
	
	private PlatoPedidoDTO prDTO;
	
	@BeforeEach
	void setup() {
		
		platoPedidoConverter= new PlatoPedidoConverter();
		
		Plato plato= new Plato();
		plato.setName("albondigas");
		EstadoPlato eP= new EstadoPlato();
		eP.setName("ENCOLA");
		
		platoPedido= new PlatoPedido();
		platoPedido.setPlato(plato);
		platoPedido.setEstadoplato(eP);
		
		prDTO= new PlatoPedidoDTO();
		prDTO.setEstadoplatodto(eP.getName());
		prDTO.setPlatodto(plato.getName());
		
	}
	
	
	@Test
	void testConvertDTOToEntity() {
		PlatoPedido pr1=this.platoPedidoConverter.convertPPDTOToEntity(prDTO);
		assertEquals(prDTO.getId(), pr1.getId());
		
	}
}
