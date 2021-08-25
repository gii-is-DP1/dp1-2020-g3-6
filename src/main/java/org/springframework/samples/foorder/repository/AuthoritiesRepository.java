package org.springframework.samples.foorder.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.foorder.model.Authorities;

public interface AuthoritiesRepository extends  CrudRepository<Authorities, String>{

	@Query("SELECT a.user.username FROM Authorities a")
	List<String> findAllUsernames() throws DataAccessException;
}
