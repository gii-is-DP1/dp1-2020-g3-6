package org.springframework.samples.foorder.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.foorder.model.Ingrediente;
import org.springframework.samples.foorder.model.IngredientePedido;
import org.springframework.samples.foorder.model.PlatoPedido;
import org.springframework.samples.foorder.model.Producto;
import org.springframework.samples.foorder.repository.IngredientePedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class IngredientePedidoService {
	
	private IngredientePedidoRepository ingredientePedidoRepository;
	private ProductoService productoService;
	private PlatoPedidoService platoPedidoService;
	private IngredienteService ingredienteService;
	
	@Autowired
	public IngredientePedidoService(IngredientePedidoRepository ingredientePedidoRepository,
			ProductoService productoService, PlatoPedidoService platoPedidoService,
			IngredienteService ingredienteService) {
		super();
		this.ingredienteService = ingredienteService;
		this.platoPedidoService = platoPedidoService;
		this.ingredientePedidoRepository = ingredientePedidoRepository;
		this.productoService = productoService;
	}

	public List<IngredientePedido> findByPlatoPedidoId(Integer id) {
		Collection<IngredientePedido> ls = ingredientePedidoRepository.findAll();
		List<IngredientePedido> res = new ArrayList<IngredientePedido>();
		for (IngredientePedido l : ls) {
			if (l.getPp() != null) {
				if (l.getPp().getId() == id) {
					res.add(l);
				}
			}
		}
		return res;
	}

	@Transactional
	public IngredientePedido save(IngredientePedido ingredientePedido, 
			Integer ingId, Integer ppId) {
		ingredientePedido.setIngrediente(ingredienteService.findById(ingId).get());
		ingredientePedido.setPp(platoPedidoService.findById(ppId).get());
		
		Double cantidad = ingredientePedido.getCantidadPedida();
		Producto prod = ingredientePedido.getIngrediente().getProducto();
		prod.setCantAct(prod.getCantAct()-cantidad);
		productoService.save(prod);
		
		ingredientePedidoRepository.save(ingredientePedido);
		return ingredientePedido;
	}
	
	@Transactional
	public IngredientePedido crearIngredientePedidoPorIngrediente(Ingrediente i) {
		IngredientePedido ip = new IngredientePedido();
		ip.setCantidadPedida(i.getCantidadUsualPP());
		ip.setIngrediente(i);
		return ip;
	}
	
	@Transactional
	public Collection<IngredientePedido> CrearIngredientesPedidos(PlatoPedido pp) {
		Collection<Ingrediente> ingredienteList = pp.getPlato().getIngredientes();
		Collection<IngredientePedido> lista2 = pp.getIngredientesPedidos();
		Collection<Ingrediente> lista3 = new ArrayList<>();
		if (lista2 != null) {
			Iterator<IngredientePedido> iterator2 = lista2.iterator();
			while (iterator2.hasNext()) {
				IngredientePedido ingredienteEnPP = iterator2.next();
				lista3.add(ingredienteEnPP.getIngrediente());
			}
		}
		Collection<IngredientePedido> res = new ArrayList<>();
		Iterator<Ingrediente> iterator = ingredienteList.iterator();
		while (iterator.hasNext()) {
			Ingrediente i = iterator.next();
			if (!(lista3.contains(i))) {
				IngredientePedido ingp = crearIngredientePedidoPorIngrediente(i);
				ingp.setPp(pp);
				res.add(ingp);
			}
		}
		return res;
	}
}
