package org.springframework.samples.petclinic.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Proveedor;
import org.springframework.samples.petclinic.repository.ProveedorRepository;
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
	
	public Proveedor findByName(String nombre){
		return (Proveedor) proveedorRepository.findByName(nombre);
	}
	
	public Iterable<Proveedor> findActivos(){
		return proveedorRepository.findByActivoTrue();
	}
	
	public List<String> findActivosName(){
		return proveedorRepository.findActivosName();
	}
	
	@Transactional(readOnly = true)
	public boolean esIgual(String nombre){
		Proveedor proveedor = proveedorRepository.findByName(nombre);
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
		Proveedor proveedorfinal = findByName(proveedor.getName());
		if (proveedorfinal != null) {
			proveedorfinal.setActivo(true);
			proveedorfinal.setGmail(proveedor.getGmail());
			proveedorfinal.setTelefono(proveedor.getTelefono());
			proveedorRepository.save(proveedorfinal);
			log.info(String.format("Provider with name %s has been saved", proveedorfinal.getName()));
		} else {
			proveedor.setActivo(true);
			proveedorRepository.save(proveedor);
			log.info(String.format("Provider with name %s has been unhidden", proveedor.getName()));
		}
	}
	
	@Transactional
	public void deleteById(Integer id) {
		Proveedor proveedor = proveedorRepository.findById(id).get();
		proveedorRepository.deleteById(id);
		log.info(String.format("Provider with name %s has been deleted", proveedor.getName()));
	}
}
