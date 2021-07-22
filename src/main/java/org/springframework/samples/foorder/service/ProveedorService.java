package org.springframework.samples.foorder.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.foorder.model.Proveedor;
import org.springframework.samples.foorder.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class ProveedorService {
	
	private ProveedorRepository proveedorRepository;
	
	@Autowired
	public ProveedorService(ProveedorRepository proveedorRepository) {
		super();
		this.proveedorRepository = proveedorRepository;
	}

	public Iterable<Proveedor> findAll(){
		return proveedorRepository.findAll();
	}
	
	public List<String> findAllNames(){
		return proveedorRepository.findAllNames();
	}
	
	public Optional<Proveedor> findById(Integer id) {
		return proveedorRepository.findById(id);
	}
	
	public Optional<Proveedor> findByName(String nombre){
		return proveedorRepository.findByName(nombre);
	}
	
	public Iterable<Proveedor> findActivos(){
		return proveedorRepository.findByActivoTrue();
	}
	
	public List<String> findActivosName(){
		return proveedorRepository.findActivosName();
	}
	
	@Transactional(readOnly = true)
	public boolean esIgual(String nombre){
		Proveedor proveedor = proveedorRepository.findByName(nombre).get();
		if(proveedor==null) {
			return false;
		}
		else if(proveedor!=null && proveedor.getActivo()==false){
			return false;
		}
		else{
			return true;
		}
	}

	@Transactional
	public void save(Proveedor proveedor) {
		if (findByName(proveedor.getName()).isPresent()) {
			Proveedor proveedorFinal = findByName(proveedor.getName()).get();
			proveedorFinal.setActivo(true);
			proveedorFinal.setGmail(proveedor.getGmail());
			proveedorFinal.setTelefono(proveedor.getTelefono());
			proveedorRepository.save(proveedorFinal);
			log.info(String.format("Provider with name %s has been updated", proveedorFinal.getName()));
		} else {
			proveedor.setActivo(true);
			proveedorRepository.save(proveedor);
			log.info(String.format("Provider with name %s has been created", proveedor.getName()));
		}
	}
	
	@Transactional
	public void hide(Proveedor proveedor) {
		proveedor.setActivo(false);
		log.info(String.format("Provider with name %s has been hidden", proveedor.getName()));
		proveedorRepository.save(proveedor);
	}
	
	@Transactional
	public Boolean unhide(Proveedor proveedor) {
		Proveedor proveedorDB = proveedorRepository.findByName(proveedor.getName()).get();
		proveedorDB.setActivo(true);
		proveedorDB.setGmail(proveedor.getGmail());
		proveedorDB.setTelefono(proveedor.getTelefono());
		log.info(String.format("Provider with name %s has been unhidden and updated", proveedor.getName()));
		proveedorRepository.save(proveedorDB);
		return true;
	}
}
