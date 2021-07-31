package org.springframework.samples.foorder.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.foorder.model.User;

public interface UserRepository extends  CrudRepository<User, String>{

	@Query("SELECT aut.authority FROM Authorities aut WHERE aut.user.username LIKE :username")
	public String findAuthoritiesByUsername(@Param("username") String username);
	
}
