package org.springframework.samples.foorder.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.samples.foorder.model.ProductoDTO;
import org.springframework.samples.foorder.model.Proveedor;
import org.springframework.samples.foorder.model.TipoProducto;
import org.springframework.samples.foorder.service.PedidoService;
import org.springframework.samples.foorder.service.ProductoService;
import org.springframework.samples.foorder.service.ProveedorService;
import org.springframework.samples.foorder.service.TipoProductoService;
import org.springframework.samples.foorder.validators.ProductoValidator;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Test class for {@link ProductoController}
 *
 * @author Alexander
 */

@WebMvcTest(controllers=ProductoController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
class ProductoControllerTests {
	
	
	private static final int TEST_PRODUCTO1_ID  = 1;
	private static final int TEST_PRODUCTO2_ID  = 2;

	@MockBean
	private ProductoService productoService;
	@MockBean
	private ProveedorService proveedorService;
	@MockBean
	private TipoProductoService tipoProductoService;
	@MockBean
	private PedidoService pedidoService;
	@MockBean
	private ProductoConverter productoConverter;
	@MockBean
	private TipoProductoFormatter tipoProductoFormatter;
	@MockBean
	private ProveedorFormatter proveedorFormatter;
	@MockBean
	private ProductoValidator productoValidator;

	@Autowired
	private MockMvc mockMvc;
	
	private Producto producto1;
	private Producto producto2;
	private ProductoDTO productoConvertido;
	private TipoProducto tipoproducto;
	private Proveedor proveedor1;
	private Proveedor proveedor2;
	private LineaPedido lineapedido;
	private Pedido pedido;
	
	private Collection<Producto> lProducto;
	private Iterable<Producto> itProducto;
	private Collection<Producto> lProdFaltan;
	private Iterable<Producto> itProdFaltan;
	private Collection<Producto> lPorProveedor1;
	private Collection<Producto> lPorProveedor2;
	
	private List<String> lProveedor;

	@BeforeEach
	void test() throws ParseException {
		
		tipoproducto = new TipoProducto();
		//Proveedor 1
		proveedor1 = new Proveedor();
		proveedor1.setId(1);
		proveedor1.setName("proveedor_1");
		proveedor1.setGmail("proveedor_1@gmail.com");
		proveedor1.setTelefono("123456789");

		//Proveedor 2
		proveedor2 = new Proveedor();
		proveedor2.setId(1);
		proveedor2.setName("proveedor_2");
		proveedor2.setGmail("proveedor_2@gmail.com");
		proveedor2.setTelefono("987654321");
		
		//Produto 1
		producto1 = new Producto();
		producto1.setId(TEST_PRODUCTO1_ID);
		producto1.setCantAct(10.0);
		producto1.setCantMax(11.0);
		producto1.setCantMin(1.0);
		producto1.setName("producto_1");
		producto1.setProveedor(proveedor1);
		producto1.setTipoProducto(tipoproducto);
		
		//Producto 2
		producto2 = new Producto();
		producto2.setId(TEST_PRODUCTO2_ID);
		producto2.setCantAct(2.0);
		producto2.setCantMax(11.0);
		producto2.setCantMin(3.0);
		producto2.setName("producto_2");
		producto2.setProveedor(proveedor2);
		producto2.setTipoProducto(tipoproducto);
		
		productoConvertido = productoConverter.convertEntityToProductoDTO(producto2);
		
		//Pedidos 1
		pedido = new Pedido();
		pedido.setId(1);
		pedido.setProveedor(proveedor1);
		pedido.setHaLlegado(false);
		pedido.setFechaEntrega(null);
		pedido.setFechaPedido(LocalDate.of(2021, 07, 10));
		
		//Linea Pedido 1
		lineapedido = new LineaPedido();
		lineapedido.setId(1);
		lineapedido.setCantidad(2);
		lineapedido.setProducto(producto1);
		lineapedido.setPedido(pedido);
		
		//Lista producto
		lProducto = new ArrayList<Producto>();
		lProducto.add(producto1);
		lProducto.add(producto2);
		itProducto = lProducto;
		
		lProdFaltan = new ArrayList<Producto>();
		lProdFaltan.add(producto2);
		itProdFaltan = lProdFaltan;
				
		lPorProveedor1 = new ArrayList<Producto>();
		lPorProveedor1.add(producto1);
		
		lPorProveedor2 = new ArrayList<Producto>();
		lPorProveedor2.add(producto2);
		
		//Lista Proveedor
		
		lProveedor = new ArrayList<String>();
		lProveedor.add(proveedor1.getName());
		lProveedor.add(proveedor2.getName());
		
		
		
		given(this.productoService.findById(TEST_PRODUCTO1_ID)).willReturn(Optional.of(producto1));
		given(this.productoService.findById(TEST_PRODUCTO2_ID)).willReturn(Optional.of(producto2));
		given(this.productoService.findAll()).willReturn(itProducto);
		given(this.productoService.findByProveedor(producto1)).willReturn(lPorProveedor1);
		given(this.productoService.findByProveedor(producto2)).willReturn(lPorProveedor2);
		given(this.proveedorService.findActivosName()).willReturn(lProveedor);
		given(this.tipoProductoService.findAll()).willReturn(new ArrayList<TipoProducto>());
		given(this.proveedorService.findAllNames()).willReturn(lProveedor);
		given(this.productoConverter.convertProductoDTOToEntity(productoConvertido)).willReturn(producto2);
		given(this.tipoProductoFormatter.parse("Otros", Locale.ENGLISH)).willReturn(tipoproducto);
		given(this.proveedorFormatter.parse("proveedor_2", Locale.ENGLISH)).willReturn(proveedor2);
		
		given(this.productoConverter.convertEntityToProductoDTO(any())).willReturn(new ProductoDTO());
	}
	
	// Test listado productos
	
	@WithMockUser(value = "spring")
	@Test
	void testListadoProducto() throws Exception {
		mockMvc.perform(get("/producto"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("producto"))
			.andExpect(model().attribute("producto", is(this.itProducto)))
			.andExpect(view().name("producto/listaProducto"));
	}
		
	// Test listado productos que faltan.
	@WithMockUser(value = "spring")
	@Test
	void testListadoProductoQueFaltan() throws Exception {
		mockMvc.perform(get("/producto//notificaciones"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("producto"))
			.andExpect(model().attribute("producto", is(this.itProdFaltan)))
			.andExpect(view().name("producto/notificaciones"));
	}	
	
	// Test positivo crear producto.
	
	@WithMockUser(value = "spring")
	@Test
	void testCrearProducto() throws Exception {
		mockMvc.perform(get("/producto/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("producto/editProducto"))
				.andExpect(model().attributeExists("producto"))
				.andExpect(model().attributeExists("listaProveedores"))
				.andExpect(model().attribute("listaProveedores", is(this.lProveedor)));
	}
	
	// Test positivo guardar producto.
	
	@WithMockUser(value = "spring")
	@Test
	@DisplayName("guardar producto ")
	void testCreationForm() throws Exception {
		BDDMockito.given(this.productoConverter.convertProductoDTOToEntity(any())).willReturn(producto1);
		this.mockMvc.perform(MockMvcRequestBuilders.post("/producto/save").with(csrf())
		.param("name", "espinacas")
		.param("tipoproductodto", "Otros")
		.param("cantMin", "1")
		.param("cantAct", "2")
		.param("cantMax", "3")
		.param("proveedor", "pedro"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/producto?message=Guardado correctamente"));
	}


	
	// Test negativo guardar producto.
	
	@WithMockUser(value = "spring")
	@Test
	@DisplayName("guardar producto negativo ")
	void testCreationNegativeForm() throws Exception {
		BDDMockito.given(this.productoConverter.convertProductoDTOToEntity(any())).willReturn(producto1);
		this.mockMvc.perform(MockMvcRequestBuilders.post("/producto/save").with(csrf())
		.param("name", "")
		.param("tipoproductodto", "Otros")
		.param("cantMin", "1")
		.param("cantAct", "2")
		.param("cantMax", "3")
		.param("proveedor", "pedro")
		.with(csrf())).andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("producto/editProducto"));
	}
	// Test positivo borrar producto.

	@WithMockUser(value = "spring")
	@Test
	void testBorrarProductoSuccess() throws Exception {
		mockMvc.perform(get("/producto/delete/{productoId}", TEST_PRODUCTO2_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/producto?message=Borrado Correctamente"));
	}
	
	// Test negativo borrar producto.
	
	@WithMockUser(value = "spring")
	@Test
	void testBorrarProductoFail() throws Exception {
		mockMvc.perform(get("/producto/delete/{productoId}", 70))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/producto?message=Producto no encontrado"));
	}
	
	// Test positivo initUpdate producto.
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateProductoSuccess() throws Exception {
		mockMvc.perform(get("/producto/edit/{productoId}", TEST_PRODUCTO2_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("producto/editarProducto"));
	}

	
	// Test positivo processUpdate producto.

	@WithMockUser(value = "spring")
	@Test
	@DisplayName("Editar producto ")
	void testEditForm() throws Exception {
		BDDMockito.given(this.productoConverter.convertProductoDTOToEntity(any())).willReturn(producto1);
		this.mockMvc.perform(MockMvcRequestBuilders.post("/producto/edit").with(csrf())
		.param("name", "espinacas")
		.param("tipoproductodto", "Otros")
		.param("cantMin", "1")
		.param("cantAct", "2")
		.param("cantMax", "3")
		.param("proveedor", "pedro"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/producto?message=Guardado Correctamente"));
	}
	// Test negativo processUpdate producto.
	@WithMockUser(value = "spring")
	@Test
	@DisplayName("Editar producto negativo ")
	void testEditNegativeForm() throws Exception {
		BDDMockito.given(this.productoConverter.convertProductoDTOToEntity(any())).willReturn(producto1);
		this.mockMvc.perform(MockMvcRequestBuilders.post("/producto/edit").with(csrf())
		.param("name", "")
		.param("tipoproductodto", "Otros")
		.param("cantMin", "1")
		.param("cantAct", "2")
		.param("cantMax", "3")
		.param("proveedor", "pedro"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.view().name("producto/editarProducto"));
	}
	
	// Test positivo guardar pedido. 

	@WithMockUser(value = "spring")
	@Test
	void testGuardarPedidoSuccess() throws Exception {
		mockMvc.perform(get("/producto/savePedido/{productoId}", TEST_PRODUCTO2_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/producto?message=Se ha creado el pedido correctamente"));
	}
	
	// Test negativo guardar pedido. 
	
	@WithMockUser(value = "spring")
	@Test
	void testGuardarPedidoFail() throws Exception {
		mockMvc.perform(get("/producto/savePedido/{productoId}", 70))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/producto?message=Producto no encontrado"));
	}
	
}