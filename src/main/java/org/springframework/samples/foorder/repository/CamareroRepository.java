package org.springframework.samples.foorder.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.foorder.model.Camarero;

public interface CamareroRepository extends CrudRepository<Camarero, Integer>{
	
    Camarero findByUsuario(String user);

}
