package org.springframework.samples.foorder.repository;


import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.foorder.model.EstadoPlato;

public interface EstadoPlatoRepository extends CrudRepository<EstadoPlato, Integer>{
	
	Collection<EstadoPlato> findAll();

}
