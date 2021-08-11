package org.springframework.samples.foorder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.foorder.model.LineaPedido;
import org.springframework.samples.foorder.model.Pedido;
import org.springframework.samples.foorder.model.Producto;
import org.springframework.samples.foorder.repository.LineaPedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LineaPedidoService {
	
	private LineaPedidoRepository lineaPedidoRepository;
	
	@Autowired
	public LineaPedidoService(LineaPedidoRepository lineaPedidoRepository) {
		super();
		this.lineaPedidoRepository = lineaPedidoRepository;
	}
	
	public Iterable<LineaPedido> findByPedidoId(int pedidoID) {
		return lineaPedidoRepository.findByPedidoId(pedidoID);
	}
	
	@Transactional
	public void save(LineaPedido lineaPedido) throws DataAccessException {
		lineaPedidoRepository.save(lineaPedido);
		log.info(String.format("Order with product %s has been created", lineaPedido.getProducto().getName()));
	}

	@Transactional
	public LineaPedido anadirLineaPedido(Producto producto, Pedido pedido) throws DataAccessException {
		LineaPedido res = new LineaPedido();
		Integer cantidad = (int) (producto.getCantMax()-producto.getCantAct());
		res.setProducto(producto);
		res.setCantidad(cantidad);
		res.setPedido(pedido);
		return res;
	}
}
