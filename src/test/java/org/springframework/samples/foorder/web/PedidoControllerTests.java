package org.springframework.samples.foorder.web;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.foorder.configuration.SecurityConfiguration;
import org.springframework.samples.foorder.model.Pedido;
import org.springframework.samples.foorder.model.Proveedor;
import org.springframework.samples.foorder.service.PedidoService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Test class for {@link PedidoController}
 *
 * @author Alexander
 */

@WebMvcTest(controllers=PedidoController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
class PedidoControllerTests {
	
	private static final int TEST_PEDIDO_ID = 1;
	private static final int TEST_PEDIDO_ID2 = 2;
	
	private static final int TEST_PROVEEDOR_ID = 1;

	@MockBean
	private PedidoService pedidoService;

	@Autowired
	private MockMvc mockMvc;
	
	private Pedido pedido;
	private Pedido pedido2;
	private Proveedor proveedor;
	
	private Collection<Pedido> lPorDia;
	private Collection<Pedido> lPedidos;
	private Iterable<Pedido> itPedidos;

	@BeforeEach
	void test() {
		
		//Proveedor
		proveedor = new Proveedor();
		proveedor.setId(TEST_PROVEEDOR_ID);
		proveedor.setName("jorge");
		proveedor.setGmail("jorge@gmail.com");
		proveedor.setTelefono("678678678");
		
		//Pedidos 1
		pedido = new Pedido();
		pedido.setId(TEST_PEDIDO_ID);
		pedido.setProveedor(proveedor);
		pedido.setHaLlegado(false);
		pedido.setFechaEntrega(null);
		pedido.setFechaPedido(LocalDate.of(2021, 07, 10));
		
		//Pedido 2
		pedido2 = new Pedido();
		pedido2.setId(TEST_PEDIDO_ID2);
		pedido2.setProveedor(proveedor);
		pedido2.setHaLlegado(true);
		pedido2.setFechaEntrega(LocalDate.now());
		pedido2.setFechaPedido(LocalDate.of(2021, 01, 30));
		
		//Lista pedidos
		lPedidos = new ArrayList<Pedido>();
		lPedidos.add(pedido);
		lPedidos.add(pedido2);
		itPedidos = lPedidos;
		
		lPorDia = new ArrayList<Pedido>();
		lPorDia.add(pedido2);
		
		given(this.pedidoService.findById(TEST_PEDIDO_ID)).willReturn(Optional.of(pedido));
		given(this.pedidoService.findById(TEST_PEDIDO_ID2)).willReturn(Optional.of(pedido2));
		given(this.pedidoService.findAll()).willReturn(itPedidos);
		given(this.pedidoService.encontrarPedidoDia("2021-01-30")).willReturn(lPorDia);
		
	}
	
	//Test Listar Pedido
	@WithMockUser(value = "spring")
	@Test
	void testListadoPedido() throws Exception {
		mockMvc.perform(get("/pedidos"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("pedido"))
			.andExpect(model().attribute("pedido", is(this.lPedidos) ))
			.andExpect(view().name("pedidos/listaPedidos"));
	}
	
	
	//Test Listar Pedido por dia
	@WithMockUser(value = "spring")
	@Test
	void testListadoPedidoPorDia() throws Exception {
		mockMvc.perform(get("/pedidos/listaPedidoTotal/dia")
			.param("date","2021-01-30"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("pedido"))
			.andExpect(model().attribute("pedido", is(this.lPorDia)))
			.andExpect(view().name("pedidos/listaPedidos"));
	}	
	
	// Test Positivo para Recargar Stock
	
	@WithMockUser(value = "spring")
    @Test
    void testRecargarStockSuccess() throws Exception {
		mockMvc.perform(get("/pedidos/terminarPedido/{pedidoID}", TEST_PEDIDO_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/pedidos?message=Se ha finalizado el pedido correctamente"));
	}	
	
	
	// Test Negativo para Recargar Stock 
	
	@WithMockUser(value = "spring")
    @Test
    void testRecargarStockFail1() throws Exception {
		mockMvc.perform(get("/pedidos/terminarPedido/{pedidoID}", TEST_PEDIDO_ID2))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/pedidos?message=El pedido ya se ha finalizado"));
	}	
	
	@WithMockUser(value = "spring")
    @Test
    void testRecargarStockFail2() throws Exception {
		mockMvc.perform(get("/pedidos/terminarPedido/{pedidoID}", 80))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/pedidos?message=El pedido no se ha encontrado"));
	}	

}
