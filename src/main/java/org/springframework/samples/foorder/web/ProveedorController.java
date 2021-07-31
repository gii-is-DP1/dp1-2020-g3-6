package org.springframework.samples.foorder.web;

import java.util.Iterator;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.foorder.model.Manager;
import org.springframework.samples.foorder.model.Proveedor;
import org.springframework.samples.foorder.service.ProveedorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/proveedor")
public class ProveedorController {

	private ProveedorService proveedorService;
	
	@Autowired
	public ProveedorController(ProveedorService proveedorService) {
		super();
		this.proveedorService = proveedorService;
	}

	@GetMapping()
	public String listadoDeProveedores(ModelMap modelMap) {
		String vista = "proveedor/listadoDeProveedores";
		Iterable<Proveedor> proveedor = proveedorService.findActivos();
		Iterator<Proveedor> it_proveedor = proveedor.iterator();

		if (!(it_proveedor.hasNext())) {
			modelMap.addAttribute("message",
					"No hay proveedores, necesitas alguno para obtener productos, añade uno nuevo");
		}
		modelMap.addAttribute("proveedor", proveedor);
		return vista;
	}

	@GetMapping(path = "/new")
	public String crearProveedor(ModelMap modelMap) {
		String view = "proveedor/editProveedor";
		modelMap.addAttribute("proveedor", new Proveedor());
		return view;
	}

	@PostMapping(path = "/save")
	public String guardarProveedor(@Valid Proveedor proveedor, BindingResult result, ModelMap modelMap) {
		String view = "proveedor/listadoDeProveedores";
		if (result.hasErrors()) {
			log.info(String.format("Provider with name %s wasn't able to be created", proveedor.getName()));
			modelMap.addAttribute("proveedor", proveedor);
			return "proveedor/editProveedor";
		}else {
			//Si existe y está oculto, mostrar de nuevo y actualizar
			if(proveedorService.findAllNames().contains(proveedor.getName())
					&& proveedorService.findByName(proveedor.getName()).get().getActivo().equals(false)) {
				proveedorService.unhide(proveedor);
				return "redirect:/proveedor?message=El proveedor se guardo exitosamente";
			}//Si existe y no está oculto, está repetido
			else if (proveedorService.findByName(proveedor.getName()).isPresent()) {
				modelMap.addAttribute("message", "El proveedor ya existe");
				modelMap.addAttribute("proveedor", proveedor);
				return "proveedor/editProveedor";
			}//No existe, se debe guardar en la BD
			else {
				proveedorService.save(proveedor);
				view="redirect:/proveedor?message=El proveedor se guardo exitosamente";
			}
		}
		return view;
	}

	@GetMapping(path = "/delete/{proveedorid}")
	public String borrarProveedor(@PathVariable("proveedorid") int proveedorid, ModelMap modelMap) {
		String view = "proveedor/listadoDeProveedores";
		Optional<Proveedor> proveedor = proveedorService.findById(proveedorid);
		if (proveedor.isPresent()) {
			proveedorService.hide(proveedor.get());
			view="redirect:/proveedor?message=El proveedor ha sido borrado exitosamente";
		}else {
			view="redirect:/proveedor?message=El proveedor no se ha encontrado";
		}
		return view;
	}

	@GetMapping(value = "/edit/{proveedorId}")
	public String initUpdateProveedorForm(@PathVariable("proveedorId") int proveedorId, ModelMap model) {
		Optional<Proveedor> prov=proveedorService.findById(proveedorId);
		if(prov.isEmpty()) {
			return "redirect:/proveedor?message=Ese proveedor no existe";
		}
		String vista = "proveedor/editarProveedor";
		Proveedor proveedor = proveedorService.findById(proveedorId).get();
		model.addAttribute("proveedor", proveedor);
		return vista;
	}
	
	@PostMapping(value = "/edit")
	public String processUpdateProveedorForm(@Valid Proveedor proveedor, BindingResult result, ModelMap modelMap) {
		String vista = "proveedor/editarProveedor";
		if (result.hasErrors()) {
			modelMap.addAttribute("proveedor", proveedor);
			return vista;
		}else {
			if (proveedorService.findAllNames().contains(proveedor.getName())
					&& !proveedorService.findById(proveedor.getId()).get().getName().equals(proveedor.getName())) {
				return "redirect:/proveedor/edit/"+proveedor.getId()+"?message=el proveedor ya existe";
			}else {
				proveedorService.save(proveedor);
				return "redirect:/proveedor?message=Se ha guardado satisfactoriamente";
			}
		}
	}	
}
