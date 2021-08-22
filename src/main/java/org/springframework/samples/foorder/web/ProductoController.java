package org.springframework.samples.foorder.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.foorder.model.Producto;
import org.springframework.samples.foorder.model.ProductoDTO;
import org.springframework.samples.foorder.model.TipoProducto;
import org.springframework.samples.foorder.service.PedidoService;
import org.springframework.samples.foorder.service.ProductoService;
import org.springframework.samples.foorder.service.ProveedorService;
import org.springframework.samples.foorder.service.TipoProductoService;
import org.springframework.samples.foorder.service.exceptions.DuplicatedPedidoException;
import org.springframework.samples.foorder.service.exceptions.PedidoPendienteException;
import org.springframework.samples.foorder.service.exceptions.PlatoPedidoPendienteException;
import org.springframework.samples.foorder.validators.ProductoValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping(value = "/producto")
public class ProductoController {
	
	private ProductoService productoService;
	private ProveedorService proveedorService;
	private TipoProductoService tipoProductoService;
	private PedidoService pedidoService;
	private ProductoConverter productoConverter;
	private TipoProductoFormatter tipoProductoFormatter;
	private ProveedorFormatter proveedorFormatter;
	private ProductoValidator productoValidator;
	

	
	@Autowired
	public ProductoController(ProductoService productoService, ProveedorService proveedorService,
			TipoProductoService tipoProductoService, PedidoService pedidoService, ProductoConverter productoConverter, 
			TipoProductoFormatter tipoProductoFormatter, ProveedorFormatter proveedorFormatter, ProductoValidator productoValidator) {
		super();
		this.productoService = productoService;
		this.proveedorService = proveedorService;
		this.tipoProductoService = tipoProductoService;
		this.pedidoService = pedidoService;
		this.productoConverter = productoConverter;
		this.tipoProductoFormatter = tipoProductoFormatter;
		this.proveedorFormatter = proveedorFormatter;
		this.productoValidator = productoValidator;
	}
	@InitBinder("productodto")
	public void initCamareroBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(productoValidator);
	}
	

	@ModelAttribute("tipoproducto") 				//Esto pertenece a TipoProducto
	public Collection<TipoProducto> poblarTiposProducto() {
		return this.tipoProductoService.findAll();
	}
	
	@GetMapping()
	public String listadoProducto(@RequestParam Map<String, Object> params,ModelMap modelMap) {
		int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
		PageRequest pageRequest = PageRequest.of(page, 10);
		Page<Producto> pageProducto = productoService.getAll(pageRequest);
		int totalPage = pageProducto.getTotalPages();
		if(totalPage > 0) {
			List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
			modelMap.addAttribute("pages", pages);
		}
		modelMap.addAttribute("list", pageProducto.getContent());
		modelMap.addAttribute("current", page + 1);
		modelMap.addAttribute("next", page + 2);
		modelMap.addAttribute("prev", page);
		modelMap.addAttribute("last", totalPage);
		String vista= "producto/listaProducto";
		return vista;	
	}
	
	@GetMapping(path="/notificaciones")
	public String productos_que_faltan(ModelMap modelMap) {
		String vista= "producto/notificaciones";
		Collection<Producto> lista = new ArrayList<Producto>();
		Iterable<Producto> producto = productoService.findAll();
		Iterator<Producto> iter = producto.iterator();
		while(iter.hasNext()) {
			Producto product = iter.next();
			if(product.getCantMin() > product.getCantAct()) {
				lista.add(product);
			}
		}
		Iterable<Producto> res = lista;
		modelMap.addAttribute("producto",res);
		return vista;	
	}
	
	@GetMapping(path="/new")
	public String crearProducto(ModelMap modelMap) {
		String vista= "producto/editProducto";
		Collection<TipoProducto> collectionTipoProducto = this.tipoProductoService.findAll();
		List<String> collectionProveedor = this.proveedorService.findActivosName();
		modelMap.addAttribute("producto",new ProductoDTO());
		modelMap.addAttribute("listaProveedores", collectionProveedor);
		modelMap.addAttribute("listaTipos", collectionTipoProducto);
		return vista;
	}
		
	@PostMapping(path="/save")
	public String guardarProducto(@Valid ProductoDTO producto,BindingResult result,ModelMap modelMap) throws ParseException {
		String vista= "producto/listaProducto";
		this.productoValidator.validate(producto, result);
		String message = "";
		FieldError error=this.productoService.resultProductSave(producto, result);
		if(error!=null) {
			result.addError(error);
		}
		if(result.hasErrors()) {
			log.info(String.format("Product with name %s wasn't able to be created", producto.getName()));
			Collection<TipoProducto> collectionTipoProducto = this.tipoProductoService.findAll();
			List<String> collectionProveedor = this.proveedorService.findActivosName();
			modelMap.addAttribute("producto", producto);
			modelMap.addAttribute("listaProveedores", collectionProveedor);
			modelMap.addAttribute("listaTipos", collectionTipoProducto);
			modelMap.addAttribute("org.springframework.validation.BindingResult.producto", result);
			return "producto/editProducto";
		}else {
			message="Guardado correctamente";
			if(productoService.cantidadMaximaMayor25PorCiento(producto)) {
				message="La cantidad de "+producto.getName()+" supera la cantidad m&aacutexima,intente gastarlo";
			}
			final Producto productoFinal = productoConverter.convertProductoDTOToEntity(producto);
			productoFinal.setTipoProducto(tipoProductoFormatter.parse(producto.getTipoproductodto(), Locale.ENGLISH));
			productoFinal.setProveedor(proveedorFormatter.parse(producto.getProveedor(), Locale.ENGLISH));
			productoService.save(productoFinal);
			vista="redirect:/producto?message="+message;
		}
		return vista; 
	}
		
	@GetMapping(path="/delete/{productoId}")
	public String borrarProducto(@PathVariable("productoId") int productoId, ModelMap modelMap) throws PlatoPedidoPendienteException  {
		String vista= "producto/listaProducto";
		Optional<Producto> prod= productoService.findById(productoId);
		if(prod.isPresent()) {
			try {
				productoService.deleteById(productoId);
				vista="redirect:/producto?message=Borrado Correctamente";
				
			}catch (PedidoPendienteException ex) {
				ex.getMessage();
				vista="redirect:/producto?message=No se puede borrar porque hay un pedido pendiente con ese producto";
			}catch (PlatoPedidoPendienteException ex) {
				ex.getMessage();
				vista="redirect:/producto?message=No se puede borrar porque hay un plato pendiente con ese producto";
			}
		}
		else {
			vista="redirect:/producto?message=Producto no encontrado";
		}
		return vista;
	}

	@GetMapping(value = "/edit/{productoId}")
	public String initUpdateProductoForm(@PathVariable("productoId") int productoId, ModelMap model) {		
		String vista= "producto/editarProducto";	
		Collection<TipoProducto> collectionTipoProducto = this.tipoProductoService.findAll();
		Producto producto =  productoService.findById(productoId).get();
		ProductoDTO productoConvertido = productoConverter.convertEntityToProductoDTO(producto);
		Collection<String> collectionProveedor = this.proveedorService.findAllNames();
		model.addAttribute("listaTipos", collectionTipoProducto);
		model.addAttribute("listaProveedores", collectionProveedor);
		model.addAttribute("producto", productoConvertido);
		return vista;
		}
	
	@PostMapping(value = "/edit")
	public String processUpdateProductoForm(@Param("nombreProducto") String nombreProducto,@Param("nombreProveedor") String nombreProveedor, @Valid ProductoDTO producto, BindingResult result,ModelMap modelMap) throws ParseException {
		FieldError error=this.productoService.resultProductEdit(nombreProducto, nombreProveedor ,producto , result);
		if(error!=null) {
			result.addError(error);
		}
		this.productoValidator.validate(producto, result);
		String message="";
		if(result.hasErrors()) {
			Collection<TipoProducto> collectionTipoProducto = this.tipoProductoService.findAll();
			Collection<String> collectionProveedor = this.proveedorService.findAllNames();
			producto.setName(nombreProducto);
			producto.setProveedor(nombreProveedor);
			modelMap.addAttribute("producto", producto);
			modelMap.addAttribute("listaTipos", collectionTipoProducto);
			modelMap.addAttribute("listaProveedores", collectionProveedor);
			modelMap.addAttribute("org.springframework.validation.BindingResult.producto", result);
			return "producto/editarProducto";
		}else {
			String vista="";
			message="Guardado correctamente";
			if(productoService.cantidadMaximaMayor25PorCiento(producto)) {
				message="La cantidad de "+producto.getName()+" supera la cantidad máxima,intente gastarlo";
			}
			final Producto productoFinal = productoConverter.convertProductoDTOToEntity(producto);
			productoFinal.setTipoProducto(tipoProductoFormatter.parse(producto.getTipoproductodto(), Locale.ENGLISH));
			productoFinal.setProveedor(proveedorFormatter.parse(producto.getProveedor(), Locale.ENGLISH));
			productoService.save(productoFinal);
			vista="redirect:/producto?message="+message;
			return vista;
		}
	}		
		

	@GetMapping(path="/savePedido/{productoId}")
	public String guardarPedido(@PathVariable("productoId") int productoId, ModelMap modelMap) {
		String vista= "producto/listaProducto";
		if(productoService.findById(productoId).isPresent()) {
			try {
				pedidoService.crearPedido(productoId);
				vista="redirect:/producto?message=Se ha creado el pedido correctamente";
			}catch(DuplicatedPedidoException ex){
				vista="redirect:/producto?message=Ya hay un pedido pendiente a ese proveedor";
			}
		}else {
			vista="redirect:/producto?message=Producto no encontrado";
		}
		return vista;
	}
}