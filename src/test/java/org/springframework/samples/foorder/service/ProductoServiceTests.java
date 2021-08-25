package org.springframework.samples.foorder.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.samples.foorder.model.Producto;
import org.springframework.samples.foorder.model.ProductoDTO;
import org.springframework.samples.foorder.model.Proveedor;
import org.springframework.samples.foorder.model.TipoProducto;
import org.springframework.samples.foorder.service.exceptions.PedidoPendienteException;
import org.springframework.samples.foorder.service.exceptions.PlatoPedidoPendienteException;
import org.springframework.stereotype.Service;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProductoServiceTests {

	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private ProveedorService provService;

	ProductoDTO prDTO= new ProductoDTO();
	
	Producto producto= new Producto();
	 @BeforeEach                                         
	    public void setUp() throws Exception {
			
			prDTO.setName("almejas");
			prDTO.setProveedor("Makro");
			prDTO.setTipoproductodto("carne");
			prDTO.setCantMin(1.);
			prDTO.setCantAct(2.);
			prDTO.setCantMax(3.);
			
			Proveedor prov= new Proveedor();
			prov.setName("Makro");
			prov.setGmail("pedro@gmail.com");
			prov.setTelefono("954339970");
			prov.setId(1);
			prov.setActivo(false);
			provService.save(prov);
			
			TipoProducto tp= new TipoProducto();
			tp.setId(1);
			tp.setName("carne");
			
			producto.setName("almejas");
			producto.setProveedor(prov);
			producto.setTipoProducto(tp);
			producto.setCantMin(1.);
			producto.setCantAct(2.);
			producto.setCantMax(3.);
	 }

	@Test
	void shouldFindAllProducto() {
		Collection<Producto> productos = Lists.newArrayList(productoService.findAll());
		assertThat(productos.size()).isEqualTo(19);
	}
	
	@Test
	void shouldDeleteProducto() throws DataAccessException, PedidoPendienteException, PlatoPedidoPendienteException {

		int sizeBefore = Lists.newArrayList(this.productoService.findAll()).size();

		productoService.deleteById(3);

		int sizeAfter = Lists.newArrayList(this.productoService.findAll()).size();
		assertEquals(sizeBefore-1, sizeAfter);
	}
	
	@Test
	void shouldCantidadMaximaMayor25PorCiento() {
			ProductoDTO prDTO= new ProductoDTO();
			prDTO.setName("almejas");
			prDTO.setProveedor("Makro");
			prDTO.setTipoproductodto("carne");
			prDTO.setCantMin(1.);
			prDTO.setCantAct(4.);
			prDTO.setCantMax(3.);
			Boolean res=this.productoService.cantidadMaximaMayor25PorCiento(prDTO);
			assertTrue(res);
	}
	
	@Test
	void shouldFalseCantidadMaximaMayor25PorCiento() {
		
			Boolean res=this.productoService.cantidadMaximaMayor25PorCiento(prDTO);
			assertFalse(res);
	}
	
	
	@Test
	void shouldResultProductSave() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.productoService.save(producto);
		Errors constraintViolations =  new BeanPropertyBindingResult(prDTO, "");
		FieldError error= this.productoService.resultProductSave(prDTO,  (BindingResult) constraintViolations);
		((AbstractBindingResult) constraintViolations).addError(error);
		assertThat(constraintViolations.hasErrors()).isTrue();
	}
	
	@Test
	void shouldNotCreateCamareroUserSaveDuplicatedReturnNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Errors constraintViolations =  new BeanPropertyBindingResult(prDTO, "");
		FieldError error= this.productoService.resultProductSave(prDTO, (BindingResult) constraintViolations);
		assertThat(error).isNull();
	}
	@Test
    void shouldFindbyIdProducto() {
        Optional<Producto> productotest = productoService.findById(1);
        assertThat(productotest.get().getName()).isEqualTo("Pan");
    }
    @Test
    void shouldFindbyProveedorProducto() {
        Collection<Producto> productotest = productoService.findByProveedor(producto);
        assertThat(productotest.size()).isEqualTo(7);
    }
    @Test
    void shouldsaveProducto() {
        Collection<Producto> productos = Lists.newArrayList(productoService.findAll());
        String antes = productos.toString();
        productoService.save(producto);
        Collection<Producto> productosdespues = Lists.newArrayList(productoService.findAll());
        String despues = productosdespues.toString();
        assertThat(antes.contains("almejas")).isEqualTo(false);
        assertThat(despues.contains("almejas")).isEqualTo(true);
    }
    
    @Test
    void shouldgetAll() {
    	List<Producto>lProducto = new ArrayList<Producto>();
		lProducto.add(producto);
    	PageRequest pageRequest = PageRequest.of(10, 10);
		Page<Producto>page=this.productoService.getAll(pageRequest);
		assertEquals(page.getSize(), 10);
		
    }

    @Test
    void shouldDeleteProducto2If() throws DataAccessException, PedidoPendienteException, PlatoPedidoPendienteException {
        assertThrows(PedidoPendienteException.class, () -> {
            productoService.deleteById(1);
        });
    }
    
    @Test
    void shouldDeleteProducto3If() throws DataAccessException, PedidoPendienteException, PlatoPedidoPendienteException {
        assertThrows(PlatoPedidoPendienteException.class, () -> {
            productoService.deleteById(17);
        });
    }
    
    @Test
	void shouldResultProductEditReturnNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.productoService.save(producto);
		Errors constraintViolations =  new BeanPropertyBindingResult(prDTO, "");
		FieldError error= this.productoService.resultProductEdit("almejas", "Makro", prDTO,  (BindingResult) constraintViolations);
		((AbstractBindingResult) constraintViolations).addError(error);
		assertThat(constraintViolations.hasErrors()).isTrue();
	}
    
    @Test
	void shouldResultProductEdit() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.productoService.save(producto);
		Errors constraintViolations =  new BeanPropertyBindingResult(prDTO, "");
		FieldError error= this.productoService.resultProductEdit("almejass", "Makao", prDTO,  (BindingResult) constraintViolations);
		((AbstractBindingResult) constraintViolations).addError(error);
		assertThat(constraintViolations.hasErrors()).isTrue();
	}
}
