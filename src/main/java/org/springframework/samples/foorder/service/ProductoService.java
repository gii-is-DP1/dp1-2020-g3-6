package org.springframework.samples.foorder.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.foorder.model.Ingrediente;
import org.springframework.samples.foorder.model.LineaPedido;
import org.springframework.samples.foorder.model.Producto;
import org.springframework.samples.foorder.model.ProductoDTO;
import org.springframework.samples.foorder.model.Proveedor;
import org.springframework.samples.foorder.repository.IngredienteRepository;
import org.springframework.samples.foorder.repository.LineaPedidoRepository;
import org.springframework.samples.foorder.repository.ProductoRepository;
import org.springframework.samples.foorder.service.exceptions.PedidoPendienteException;
import org.springframework.samples.foorder.service.exceptions.PlatoPedidoPendienteException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class ProductoService {
	
	private ProductoRepository productoRepository;
	private LineaPedidoRepository lineaPedidoRepository;
	private IngredienteRepository ingredienteRepository;
	
	@Autowired
	public ProductoService(ProductoRepository productoRepository, LineaPedidoRepository lineaPedidoRepository, IngredienteRepository ingredienteRepository) {
		super();
		this.productoRepository = productoRepository;
		this.lineaPedidoRepository = lineaPedidoRepository;
		this.ingredienteRepository = ingredienteRepository;
	}

	public Iterable<Producto> findAll() throws DataAccessException {
		return productoRepository.findAll();
	}
	
	public Page<Producto> getAll(Pageable pageable) throws DataAccessException {
		return productoRepository.findAll(pageable);
	}	

	public Optional<Producto> findById(Integer id) {
		return productoRepository.findById(id);
	}
	
	//Esto se usa al realizar un pedido pues se necesita la lista de productos con el mismo proveedor
	public Collection<Producto> findByProveedor(Producto producto) throws DataAccessException {
		Proveedor proveedor = producto.getProveedor();
		return productoRepository.findByProveedor(proveedor);
	}

	@Transactional
	public Producto save(Producto producto) {
		log.info(String.format("Product with name %s has been saved", producto.getName()));
		return productoRepository.save(producto);
	}
	
	@Transactional(rollbackFor = { PedidoPendienteException.class ,PlatoPedidoPendienteException.class})
	public void deleteById(Integer id) throws DataAccessException, PedidoPendienteException, PlatoPedidoPendienteException{
		Iterable<LineaPedido> LineasPedido = lineaPedidoRepository.findByProductoId(id);
		Iterator<LineaPedido> it = LineasPedido.iterator();
		Boolean HaypedidoPendiente = false;
		while(it.hasNext()) {
			LineaPedido p = it.next();
		    if (p.getPedido().getHaLlegado()==false) {
		    	HaypedidoPendiente = true;
	    	}		
		}if (HaypedidoPendiente)  {    
			log.info(String.format("Excepcion de pedido pendiente "));
			throw new PedidoPendienteException();}
		else if(!(checkPlatoPedido(id))){
			log.info(String.format("Excepcion de plato pedido pendiente"));
			throw new PlatoPedidoPendienteException();}
		else{
		Producto producto = productoRepository.findById(id).get();
		productoRepository.deleteById(id);
		log.info(String.format("Producto con nombre %s ha sido borrado", producto.getName()));
		}
	}
	
	@Transactional
	private Boolean checkPlatoPedido(Integer id){
		Boolean res = true;
		Iterable<Ingrediente> ingredientes = ingredienteRepository.findByProductoId(id);
		Iterator<Ingrediente> ins = ingredientes.iterator();
		while (ins.hasNext()) {
			Ingrediente p = ins.next();
			if(!(p.sePuedeEliminarPP())) {
				res = false;
				break;
			}
		}
		return res;
	}
	
	@Transactional
	public Boolean cantidadMaximaMayor25PorCiento(ProductoDTO producto) {
		Boolean res=false;
		if(producto.getCantAct()>producto.getCantMax()) {
			res=true;
		}
		return res;
	}

	
	@Transactional(readOnly = true)
	public FieldError resultProductSave(ProductoDTO producto, BindingResult result) throws DataAccessException {
		Boolean comprueba= false;
		String productoMinus=producto.getName().toLowerCase();
		for(Producto p : this.productoRepository.findAll()) {
			if(p.getName().toLowerCase().equals(productoMinus)&&producto.getProveedor().equals(p.getProveedor().getName())) {
				comprueba=true;
			}
		}
		if(comprueba) {
			FieldError error= new FieldError("productodto", "name", producto.getName(), false, null, null, "este producto se encuentra en la lista");
			return error;
		}
		return null;
	}
	
	
	@Transactional(readOnly = true)
	public FieldError resultProductEdit(String name, String nombreProveedor,ProductoDTO producto, BindingResult result) throws DataAccessException {
		Boolean comprueba= false;
		String productoMinus=producto.getName().toLowerCase();
		for(Producto p : this.productoRepository.findAll()) {
			if(p.getName().toLowerCase().equals(productoMinus)&&producto.getProveedor().equals(p.getProveedor().getName())) {
				comprueba=true;
			}
		}
		if(comprueba&&!(name.toLowerCase().equals(productoMinus)&&nombreProveedor.equals(producto.getProveedor()))) {
			FieldError error= new FieldError("productodto", "name", producto.getName(), false, null, null, "este producto se encuentra en la lista");
			return error;
		}
		return null;
	}

	

	
	
}