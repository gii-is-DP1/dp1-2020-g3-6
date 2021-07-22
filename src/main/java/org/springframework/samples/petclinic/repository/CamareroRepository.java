package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Camarero;

public interface CamareroRepository extends CrudRepository<Camarero, Integer>{
	
	@Query("SELECT a FROM Camarero a WHERE a.usuario = ?1")
	Camarero findByUser(String user);

}
