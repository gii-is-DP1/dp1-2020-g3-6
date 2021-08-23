package org.springframework.samples.foorder.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.foorder.model.PlatoPedido;
import org.springframework.samples.foorder.repository.PlatoPedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlatoPedidoService {
	
	private PlatoPedidoRepository platoPedidoRepository;
	
	@Autowired
	public PlatoPedidoService(PlatoPedidoRepository platoPedidoRepository) {
		super();
		this.platoPedidoRepository = platoPedidoRepository;
	}

	public Iterable<PlatoPedido> findAll() {
		return platoPedidoRepository.findAll();
	}
	
	public Optional<PlatoPedido> findById(Integer id) {
		return platoPedidoRepository.findById(id);
	}

	@Transactional
	public void save(PlatoPedido pp) {
		platoPedidoRepository.save(pp);
	}
	
	public Iterable<PlatoPedido> platosPedidosDisponibles() {
		return platoPedidoRepository.platosPedidosDisponibles();
	}
}
