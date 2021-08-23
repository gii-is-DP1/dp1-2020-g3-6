package org.springframework.samples.foorder.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author Michael Isvy Simple test to make sure that Bean Validation is working
 *         (useful when upgrading to a new version of Hibernate Validator/ Bean
 *         Validation)
 */
class ValidatorTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	// TESTS PARA Propietario -----------------------------------------------------------------------

	@Test
	@DisplayName("Validar una Propietario Correcto")
	void shouldNotValidateReviewWhenValorationIncorrect() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Propietario propietario = new Propietario();
		propietario.setName("Jose");
		propietario.setApellido("Alves");
		propietario.setTelefono("600234321");
		propietario.setGmail("abdche@gmail.com");
		propietario.setUsuario("Manma");
		propietario.setContrasena("12345678");

		Validator validator = createValidator();
		Set<ConstraintViolation<Propietario>> constraintViolations = validator.validate(propietario);
		assertThat(constraintViolations.size()).isEqualTo(0);
	}

	// TESTS PARA Proveedor -----------------------------------------------------------------------

	@Test
	@DisplayName("Validar una Proveedor Correcto")
	void shouldNotValidateProveedorReviewWhenValorationIncorrect() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Proveedor Proveedor = new Proveedor();
		Proveedor.setName("juan");
		Proveedor.setTelefono("600234321");
		Proveedor.setGmail("dbfche@gmail.com");

		Validator validator = createValidator();
		Set<ConstraintViolation<Proveedor>> constraintViolations = validator.validate(Proveedor);

		assertThat(constraintViolations.size()).isEqualTo(0);

	}

	@Test
	@DisplayName("Validar un Proveedor incorrecto")
	void shouldNotValidateProveedorWhenAllFieldsIncorrect() {

		Proveedor Proveedor = new Proveedor();
		Proveedor.setName("");
		Proveedor.setTelefono("");
		Proveedor.setGmail("");

		Validator validator = createValidator();
		Set<ConstraintViolation<Proveedor>> constraintViolations = validator.validate(Proveedor);
		assertThat(constraintViolations.size()).isEqualTo(3);
		for (ConstraintViolation<Proveedor> d : constraintViolations) {
			if (d.getPropertyPath().toString().equals("name")) {
				assertThat(d.getMessage()).isEqualTo("size must be between 3 and 50");
			}
			if (d.getPropertyPath().toString().equals("apellido")) {
				assertThat(d.getMessage()).isEqualTo("size must be between 3 and 50");
			}
			if (d.getPropertyPath().toString().equals("gmail")) {
				assertThat(d.getMessage()).isEqualTo("size must be between 3 and 50");
			}
			if (d.getPropertyPath().toString().equals("telefono")) {
				assertThat(d.getMessage()).isEqualTo("size must be between 3 and 50");
			}
		}
	}

	// TESTS PARA Producto -----------------------------------------------------------------------
	
	@Test
	@DisplayName("Validar una Producto Correcto")
	void shouldNotValidateProductoReviewWhenValorationIncorrect() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Producto Producto = new Producto();
		TipoProducto TestTP = new TipoProducto();
		Producto.setName("Misco");
		Producto.setTipoProducto(TestTP);
		Producto.setCantMin(1.0);
		Producto.setCantAct(2.0);
		Producto.setCantMax(5.0);

		Validator validator = createValidator();
		Set<ConstraintViolation<Producto>> constraintViolations = validator.validate(Producto);

		assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	@DisplayName("Validar un Producto incorrecto")
	void shouldNotValidateProductoWhenAllFieldsIncorrect() {

		Proveedor Proveedor = new Proveedor();
		Proveedor.setName("");

		Validator validator = createValidator();
		Set<ConstraintViolation<Proveedor>> constraintViolations = validator.validate(Proveedor);
		assertThat(constraintViolations.size()).isEqualTo(1);
		for (ConstraintViolation<Proveedor> d : constraintViolations) {
			if (d.getPropertyPath().toString().equals("name")) {
				assertThat(d.getMessage()).isEqualTo("size must be between 3 and 50");
			}
		}
	}
}
