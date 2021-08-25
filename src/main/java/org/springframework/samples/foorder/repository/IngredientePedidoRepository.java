package org.springframework.samples.foorder.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.foorder.model.IngredientePedido;

public interface IngredientePedidoRepository extends CrudRepository<IngredientePedido, Integer>{
	
	Collection<IngredientePedido> findAll();
	
}
