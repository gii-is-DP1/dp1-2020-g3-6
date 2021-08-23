package org.springframework.samples.foorder.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.foorder.model.Ingrediente;

public interface IngredienteRepository extends CrudRepository<Ingrediente, Integer> {
	
	@Query("SELECT i FROM Ingrediente i ORDER BY i.producto.name")
	Collection<Ingrediente> findAll();
	
	Iterable<Ingrediente> findByProductoId(Integer id);
	
}

