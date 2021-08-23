/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.foorder.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.foorder.model.Ingrediente;
import org.springframework.samples.foorder.model.IngredientePedido;
import org.springframework.samples.foorder.model.PlatoPedido;
import org.springframework.samples.foorder.repository.IngredientePedidoRepository;
import org.springframework.stereotype.Service;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class IngredientePedidoServiceTests {        
	@Autowired
	protected IngredientePedidoService ingredientePedidoService;
	
	@Autowired
	protected IngredientePedidoRepository ingredientePedidoRep;
	
	@Autowired
    protected IngredienteService ingredienteService;
	
	@Autowired
    protected PlatoPedidoService platoPedidoService;
	
	@Autowired
    protected ProductoService productoService;
	
	@Test
	@Transactional
	public void crearIngredientePedidoPorIngrediente() {
		Ingrediente in = new Ingrediente();
		in.setId(1);
		in.setCantidadUsualPP(3.0);
		
		IngredientePedido ingrediente = ingredientePedidoService.crearIngredientePedidoPorIngrediente(in);
		
		assertThat(ingrediente.getIngrediente().getId()).isEqualTo(1);
		assertThat(ingrediente.getCantidadPedida()).isEqualTo(3.0);
	}       
	
	@Test
	public void shouldFindByPlatoPedidoId() {
		List<IngredientePedido> ls= this.ingredientePedidoService.findByPlatoPedidoId(1);
		assertEquals(ls.size(), 5);
	}       
	

	@Test
	public void shouldSaveIngredientePedido() {
		Ingrediente in = new Ingrediente();
		in.setId(1);
		in.setCantidadUsualPP(3.0);
		List<IngredientePedido> before= (List<IngredientePedido>) this.ingredientePedidoRep.findAll();
		IngredientePedido ingrediente = ingredientePedidoService.crearIngredientePedidoPorIngrediente(in);
		this.ingredientePedidoService.save(ingrediente, 1, 1);
		List<IngredientePedido> after= (List<IngredientePedido>) this.ingredientePedidoRep.findAll();
		assertEquals(before.size()+1, after.size());
	}       
	
	@Test
	public void shouldCrearIngredientesPedidos() {
		PlatoPedido pp=this.platoPedidoService.findById(1).get();
		Collection<IngredientePedido> ls=this.ingredientePedidoService.CrearIngredientesPedidos(pp);
		assertEquals(ls.size(),0);
	}       

}