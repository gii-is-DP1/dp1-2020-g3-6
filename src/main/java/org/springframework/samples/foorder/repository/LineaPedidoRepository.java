package org.springframework.samples.foorder.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.foorder.model.LineaPedido;

public interface LineaPedidoRepository extends CrudRepository<LineaPedido, Integer>{

	Iterable<LineaPedido> findByPedidoId(Integer pedidoId);
	
	Iterable<LineaPedido> findByProductoId(Integer productoId);
}
