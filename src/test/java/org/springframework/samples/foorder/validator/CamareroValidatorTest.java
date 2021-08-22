package org.springframework.samples.foorder.validator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.foorder.model.Camarero;
import org.springframework.samples.foorder.model.Cocinero;
import org.springframework.samples.foorder.validators.CamareroValidator;
import org.springframework.samples.foorder.validators.ProductoValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@RunWith(MockitoJUnitRunner.class)
public class CamareroValidatorTest {
	
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = 
		new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	
	@Test
	void shouldNotCreateCamareroWithWithLowerLenght() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Camarero camarero= new Camarero();
		camarero.setName("pe");
		camarero.setApellido("pe");
		camarero.setTelefono("32");
		camarero.setUsuario("pe");
		camarero.setContrasena("ab");
		Validator validator = createValidator();
		Set<ConstraintViolation<Camarero>> constraintViolations =     
				validator.validate(camarero);
		assertThat(constraintViolations.size()).isEqualTo(5);
	}
	
	@Test
	void shouldNotCreateCamareroWithWithLongerLenght() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		String muchoTexto= "qwertyuioopasdfghjklnzxcvbnmqwertyuiopasdfghjklnzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuioasdfghjklzxcvbnmqwertyuiopqwertyuiopqwertyuiopqwertyuioqqwertyuiuytrewwertyuiuytrewq";
		Camarero camarero= new Camarero();
		camarero.setName(muchoTexto);
		camarero.setApellido(muchoTexto);
		camarero.setTelefono("1234567890123567");
		camarero.setUsuario(muchoTexto);
		camarero.setContrasena(muchoTexto);
		Validator validator = createValidator();
		Set<ConstraintViolation<Camarero>> constraintViolations =     
				validator.validate(camarero);
		assertThat(constraintViolations.size()).isEqualTo(5);
	}
	
	@Test
	void shouldNotCreateCamareroBadPatterns() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Camarero camarero= new Camarero();
		camarero.setName("Pedro");
		camarero.setApellido("Camarero");
		camarero.setContrasena("abc123");
		camarero.setGmail("jmtr");
		Validator validator = createValidator();
		Set<ConstraintViolation<Camarero>> constraintViolations =     
				validator.validate(camarero);
		assertThat(constraintViolations.size()).isEqualTo(2);
	}
	
	@Test
	void shouldNotCreateCamareroShorterPhoneAndPass() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		Camarero camarero= new Camarero();
		camarero.setName("Pedro");
		camarero.setApellido("Camarero");
		camarero.setUsuario("pedrito");
		camarero.setContrasena("");
		camarero.setTelefono("");
		camarero.setGmail("jmtr@gmail.com");
		BindingResult errors = new BeanPropertyBindingResult(camarero, "");
		ValidationUtils.invokeValidator(new CamareroValidator(), camarero, errors);
		assertThat(errors.getAllErrors().size()).isEqualTo(3);
		Validator validator = createValidator();
		Set<ConstraintViolation<Camarero>> constraintViolations =     
				validator.validate(camarero);
		assertThat(constraintViolations.size()).isEqualTo(3);
	}

	
}
