package org.springframework.samples.foorder.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.foorder.model.Plato;

public interface PlatoRepository extends CrudRepository<Plato, Integer>{
	
	List<Plato> findByDisponibleTrue() throws DataAccessException;
	
}
