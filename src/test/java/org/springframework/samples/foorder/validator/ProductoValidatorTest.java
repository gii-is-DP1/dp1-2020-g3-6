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
import org.springframework.samples.foorder.validators.ProductoValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@RunWith(MockitoJUnitRunner.class)
public class ProductoValidatorTest {

	
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
		producto.setName(null);
		producto.setProveedor(null);
		producto.setTipoproductodto(null);
		producto.setCantMin(12.);
		producto.setCantAct(100.);
		producto.setCantMax(25.);
		
		BindingResult errors = new BeanPropertyBindingResult(producto, "");
		ValidationUtils.invokeValidator(new ProductoValidator(), producto, errors);
		Validator validator = createValidator();
		Set<ConstraintViolation<ProductoDTO>> constraintViolations =     
				validator.validate(producto);
		//son 6 porque tambien se cuentan las validaciones de modelos
		assertThat(constraintViolations.size()).isEqualTo(3);
		assertThat(errors.getAllErrors().size()).isEqualTo(4);
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
