package org.springframework.samples.foorder.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.foorder.model.Ingrediente;

public interface IngredienteRepository extends CrudRepository<Ingrediente, Integer> {
	
	Collection<Ingrediente> findAll();
	
//	@Query("SELECT i FROM ingrediente i  WHERE producto = ?1")
//	Iterable<Ingrediente> findPorProducto(Integer id);
	
	Iterable<Ingrediente> findByProductoId(Integer id);
	
}

