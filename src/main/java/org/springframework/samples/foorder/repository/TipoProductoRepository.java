package org.springframework.samples.foorder.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.foorder.model.TipoProducto;

public interface TipoProductoRepository extends CrudRepository<TipoProducto, Integer>{
	
	Collection<TipoProducto> findAll();
	
	@Query("SELECT p.name FROM TipoProducto p")
	Collection<String> findAllNames();

}
