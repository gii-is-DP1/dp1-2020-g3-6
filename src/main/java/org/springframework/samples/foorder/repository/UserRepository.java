package org.springframework.samples.foorder.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.foorder.model.User;

public interface UserRepository extends  CrudRepository<User, String>{
	
}
