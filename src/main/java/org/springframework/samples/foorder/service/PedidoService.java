package org.springframework.samples.foorder.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.foorder.model.LineaPedido;
import org.springframework.samples.foorder.model.Pedido;
import org.springframework.samples.foorder.model.Producto;
import org.springframework.samples.foorder.repository.PedidoRepository;
import org.springframework.samples.foorder.service.exceptions.DuplicatedPedidoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PedidoService {
	
	private PedidoRepository pedidoRepository;
	private ProductoService productoService;
	private LineaPedidoService lineaPedidoService;
	
	@Autowired
	public PedidoService(PedidoRepository pedidoRepository, ProductoService productoService,
			LineaPedidoService lineaPedidoService) {
		super();
		this.pedidoRepository = pedidoRepository;
		this.productoService = productoService;
		this.lineaPedidoService = lineaPedidoService;
	}

	public Iterable<Pedido> findAll(){
		return pedidoRepository.findAll();
	}
	
	public Optional<Pedido> findById(Integer id) {
		return pedidoRepository.findById(id);
	} 
	
	public Iterable<Pedido> findByProveedorId(int proveedorID) {
		return pedidoRepository.findByProveedorId(proveedorID);
	}
	
	public int count(){
		return (int) pedidoRepository.count();
	}
	
	@Transactional(rollbackFor = DuplicatedPedidoException.class)
	public void save(Pedido pedido) throws DataAccessException, DuplicatedPedidoException {
		Iterable<Pedido> lista = pedidoRepository.findAll();
		Iterator<Pedido> it = lista.iterator();
       	Boolean hayRepetido = false;
       	
       	if(pedido.getHaLlegado().equals(null)) {
       		pedido.setFechaPedido(LocalDate.now());
    		pedido.setHaLlegado(Boolean.FALSE);
       	}
       	
       	while(it.hasNext()) {
       		Pedido p = it.next();
			if (p.getProveedor()==pedido.getProveedor() && p.getHaLlegado()==false) {
				hayRepetido = true;
		    }		
       	}
       	if (hayRepetido)  {    
       		throw new DuplicatedPedidoException();
       	}else {
       		pedidoRepository.save(pedido);
       		log.info(String.format("Order with ID %d has been created", pedido.getId()));
       	}
	}
	
	//Esto es para crear un pedido dado un producto
	@Transactional
	public void crearPedido(Integer productoId) throws DataAccessException, DuplicatedPedidoException {
		Producto producto = productoService.findById(productoId).get();
		Collection<Producto> listaProducto = productoService.findByProveedor(producto);
		Pedido pedido = new Pedido();
		pedido.setProveedor(producto.getProveedor());
		pedido.setFechaPedido(LocalDate.now());
		pedido.setHaLlegado(Boolean.FALSE);
		save(pedido);
		LineaPedido lineaPedido = new LineaPedido();
		for(Producto p : listaProducto) {
			lineaPedido = lineaPedidoService.anadirLineaPedido(p, pedido);
			lineaPedidoService.save(lineaPedido);
		}
	}
	
	//Esto es para establecer los productos una vez se recibe un Pedido
	public Pedido recargarStock(Integer pedidoId) throws DataAccessException, DuplicatedPedidoException{
		Optional<Pedido> pedi = findById(pedidoId);
		Pedido p = pedi.get();
		Iterable<LineaPedido> lineaPedi = lineaPedidoService.findByPedidoId(pedidoId);
		Iterator<LineaPedido> lp_it = lineaPedi.iterator();
		
		//Modificacion de producto
		while (lp_it.hasNext()) {
			LineaPedido lp = lp_it.next();
			Producto prod = lp.getProducto();
			prod.setCantAct(prod.getCantAct()+lp.getCantidad());
			productoService.save(prod);
		}
		
		//Modificacion de pedido
		p.setHaLlegado(Boolean.TRUE);
		p.setFechaEntrega(LocalDate.now());
		save(p);
		return p;
	}
	
	//Esto es para encontrar los pedidos por un dia de la semana 
	@Transactional(readOnly = true)
	public Collection<Pedido> encontrarPedidoDia(String dia) throws DataAccessException {
		LocalDate actualDate = LocalDate.parse(dia);
		Collection<Pedido> res = new ArrayList<>();
		Iterable<Pedido> aux = pedidoRepository.findAll();
		Iterator<Pedido> it_aux = aux.iterator();
		while (it_aux.hasNext()) {
			Pedido pedido = it_aux.next();
			if (pedido.getFechaPedido().equals(actualDate)) {
				res.add(pedido);
			}	
		}
		return res; 
	}
}
