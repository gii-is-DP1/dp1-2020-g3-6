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
import org.springframework.samples.foorder.model.LineaPedido;
import org.springframework.samples.foorder.model.Pedido;
import org.springframework.samples.foorder.model.Producto;
import org.springframework.samples.foorder.model.Proveedor;
import org.springframework.samples.foorder.model.TipoProducto;
import org.springframework.samples.foorder.service.LineaPedidoService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Test class for {@link LineaPedidoController}
 *
 * @author Alexander
 */

@WebMvcTest(controllers=LineaPedidoController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
class LineaPedidoControllerTests {
	
	
	private static final int TEST_LINEAPEDIDO_ID = 1;
	private static final int TEST_LINEAPEDIDO_ID2 = 2;

	@MockBean
	private LineaPedidoService lineaPedidoService;

	@Autowired
	private MockMvc mockMvc;
	
	private Proveedor proveedor;
	private TipoProducto tipoproducto;
	private Producto producto;
	private Producto producto2;
	private Pedido pedido;
	private LineaPedido lineapedido;
	private LineaPedido lineapedido2;
	
	private Collection<LineaPedido> lLPedidosPorPedido;
	private Iterable<LineaPedido> itLPedidosPorPedido;

	@BeforeEach
	void test() {
		
		//Proveedor 1
		proveedor = new Proveedor();
		proveedor.setId(1);
		proveedor.setName("jorge");
		proveedor.setGmail("jorge@gmail.com");
		proveedor.setTelefono("678678678");
		
		//Produto 1
		producto = new Producto();
		producto.setId(1);
		producto.setCantAct(10.0);
		producto.setCantMax(11.0);
		producto.setCantMin(1.0);
		producto.setName("producto_1");
		producto.setProveedor(proveedor);
		producto.setTipoProducto(tipoproducto);
		
		//Producto 2
		producto2 = new Producto();
		producto2.setId(2);
		producto2.setCantAct(10.0);
		producto2.setCantMax(11.0);
		producto2.setCantMin(1.0);
		producto2.setName("producto_2");
		producto2.setProveedor(proveedor);
		producto2.setTipoProducto(tipoproducto);
		
		//Pedidos 1
		pedido = new Pedido();
		pedido.setId(1);
		pedido.setProveedor(proveedor);
		pedido.setHaLlegado(false);
		pedido.setFechaEntrega(null);
		pedido.setFechaPedido(LocalDate.of(2021, 07, 10));
		
		//Linea Pedido 1
		lineapedido = new LineaPedido();
		lineapedido.setId(TEST_LINEAPEDIDO_ID);
		lineapedido.setCantidad(2);
		lineapedido.setProducto(producto);
		lineapedido.setPedido(pedido);
		
		//Linea Pedido 2
		lineapedido2 = new LineaPedido();
		lineapedido2.setId(TEST_LINEAPEDIDO_ID2);
		lineapedido2.setCantidad(2);
		lineapedido2.setProducto(producto2);
		lineapedido2.setPedido(pedido);
	
		
		//Lista Linea pedidos
		
		lLPedidosPorPedido = new ArrayList<LineaPedido>();
		lLPedidosPorPedido.add(lineapedido);
		lLPedidosPorPedido.add(lineapedido2);
		itLPedidosPorPedido = lLPedidosPorPedido;
		
		given(this.lineaPedidoService.findByPedidoId(1)).willReturn(itLPedidosPorPedido);
		
	}
	
	//Test Listar Pedido por dia
	@WithMockUser(value = "spring")
	@Test
	void testListadoLineaPedidoPorPedido() throws Exception {
		mockMvc.perform(get("/lineaPedido/porPedido")
			.param("pedidoID", "1"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("lineaPedido"))
			.andExpect(model().attribute("lineaPedido", is(this.itLPedidosPorPedido)))
			.andExpect(view().name("lineaPedido/listaLineaPedido"));
	}	
}
