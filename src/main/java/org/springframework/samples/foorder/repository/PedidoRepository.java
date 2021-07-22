package org.springframework.samples.foorder.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.foorder.model.Pedido;

public interface PedidoRepository extends CrudRepository<Pedido, Integer>{
	
}
