package org.springframework.samples.foorder.validator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.foorder.model.ProductoDTO;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@RunWith(MockitoJUnitRunner.class)
public class ProductoValidator {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = 
		new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	@Test
	void shouldNotCreateproductoWithWithNameAndProveedorAndTipoProductoEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		ProductoDTO producto= new ProductoDTO();
		producto.setName("");
		producto.setProveedor("");
		producto.setTipoproductodto("");
		Validator validator = createValidator();
		Set<ConstraintViolation<ProductoDTO>> constraintViolations =     
				validator.validate(producto);
		//son 6 porque tambien se cuentan las validaciones de modelos
		assertThat(constraintViolations.size()).isEqualTo(6);
	}
	
	@Test
	void cantidadActualEsMayorUn25PorCientoQueLaMaxima() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		ProductoDTO producto= new ProductoDTO();
		producto.setName("albondigas");
		producto.setProveedor("Makro");
		producto.setTipoproductodto("Pedro");
		producto.setCantAct(100.);
		producto.setCantMax(25.);
		Validator validator = createValidator();
		Set<ConstraintViolation<ProductoDTO>> constraintViolations =     
				validator.validate(producto);
		//son 6 porque tambien se cuentan las validaciones de modelos
		assertThat(constraintViolations.size()).isEqualTo(1);
	}
	
}
