package org.springframework.samples.foorder.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.foorder.model.Manager;

public interface ManagerRepository extends CrudRepository<Manager, Integer> {

}
