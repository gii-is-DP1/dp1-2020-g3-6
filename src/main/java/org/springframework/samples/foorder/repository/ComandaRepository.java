package org.springframework.samples.foorder.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.foorder.model.Comanda;

public interface ComandaRepository extends CrudRepository<Comanda, Integer>{
	
	@Query("SELECT MAX(c.id) FROM Comanda c")
	Integer findLastId() throws DataAccessException;
	
}
