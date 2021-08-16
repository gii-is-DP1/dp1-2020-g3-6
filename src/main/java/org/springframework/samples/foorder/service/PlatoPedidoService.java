package org.springframework.samples.foorder.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.foorder.model.PlatoPedido;
import org.springframework.samples.foorder.repository.PlatoPedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
@Slf4j
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
	public PlatoPedido save(PlatoPedido pp) {
		if(pp.getComanda() != null & pp.getEstadoplato().getId() == 1) {
//			log.info(String.format("PlateOrder with name %s has been created for table %s", pp.getPlato().getName(), pp.getComanda().getMesa().toString()));
		}else if(pp.getComanda() != null){
//			log.info(String.format("PlateOrder with name %s has been updated to %s", pp.getPlato().getName(), pp.getEstadoplato().getName()));
		}
		return platoPedidoRepository.save(pp);
	}
	
	public Iterable<PlatoPedido> platosPedidosDesponibles() {
		return platoPedidoRepository.platosPedidosDesponibles();
	}
}
