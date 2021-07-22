package org.springframework.samples.foorder.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.foorder.model.Ingrediente;
import org.springframework.samples.foorder.model.IngredientePedido;

public interface IngredientePedidoRepository extends CrudRepository<IngredientePedido, Integer>{
	
	Collection<IngredientePedido> findAll();
	
}
