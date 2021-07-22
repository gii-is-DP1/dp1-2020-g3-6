package org.springframework.samples.foorder.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.foorder.model.Producto;
import org.springframework.samples.foorder.model.Proveedor;

public interface ProductoRepository extends CrudRepository<Producto, Integer>{
	
	Collection<Producto> findByProveedor(Proveedor proveedor) throws DataAccessException;
	
}
