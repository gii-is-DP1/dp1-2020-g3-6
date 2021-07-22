package org.springframework.samples.foorder.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.foorder.model.EstadoPlato;
import org.springframework.samples.foorder.repository.EstadoPlatoRepository;
import org.springframework.stereotype.Service;
@Service
public class EstadoPlatoService {
	
	private EstadoPlatoRepository estadoPlatoRepository;
	
	@Autowired
	public EstadoPlatoService(EstadoPlatoRepository estadoPlatoRepository) {
		super();
		this.estadoPlatoRepository = estadoPlatoRepository;
	}

	public Collection<EstadoPlato> findAll(){
		return estadoPlatoRepository.findAll();
	}
}
