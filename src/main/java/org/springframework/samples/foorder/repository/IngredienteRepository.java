package org.springframework.samples.foorder.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.foorder.model.Ingrediente;

public interface IngredienteRepository extends CrudRepository<Ingrediente, Integer> {
	
	Collection<Ingrediente> findAll();
	
}

