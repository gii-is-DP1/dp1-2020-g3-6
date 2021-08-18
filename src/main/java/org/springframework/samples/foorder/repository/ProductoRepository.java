package org.springframework.samples.foorder.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.foorder.model.Producto;
import org.springframework.samples.foorder.model.Proveedor;

public interface ProductoRepository extends CrudRepository<Producto, Integer>{
	
	Collection<Producto> findByProveedor(Proveedor proveedor) throws DataAccessException;

	Page<Producto> findAll(Pageable pageable);

	@Query("SELECT p FROM Producto p WHERE p.name=?1 AND p.proveedor.name=?2")
	Optional<Producto> findByNameAndProveedor(String name, String proveedor);
}
