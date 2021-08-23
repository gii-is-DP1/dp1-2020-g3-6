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
import org.springframework.samples.foorder.model.Cocinero;
import org.springframework.samples.foorder.validators.CocineroValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@RunWith(MockitoJUnitRunner.class)
public class CocineroValidatorTest {
	
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = 
		new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	@Test
	void shouldNotCreatecocineroWithWithLowerLenght() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Cocinero cocinero= new Cocinero();
		cocinero.setName("pe");
		cocinero.setApellido("pe");
		cocinero.setTelefono("32");
		cocinero.setUsuario("pe");
		cocinero.setContrasena("ab");
		Validator validator = createValidator();
		Set<ConstraintViolation<Cocinero>> constraintViolations =     
				validator.validate(cocinero);
		assertThat(constraintViolations.size()).isEqualTo(5);
	}
	
	@Test
	void shouldNotCreatecocineroWithWithLongerLenght() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		String muchoTexto= "qwertyuioopasdfghjklnzxcvbnmqwertyuiopasdfghjklnzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuioasdfghjklzxcvbnmqwertyuiopqwertyuiopqwertyuiopqwertyuioqqwertyuiuytrewwertyuiuytrewq";
		Cocinero cocinero= new Cocinero();
		cocinero.setName(muchoTexto);
		cocinero.setApellido(muchoTexto);
		cocinero.setTelefono("1234567890123567");
		cocinero.setUsuario(muchoTexto);
		cocinero.setContrasena(muchoTexto);
		Validator validator = createValidator();
		Set<ConstraintViolation<Cocinero>> constraintViolations =     
				validator.validate(cocinero);
		assertThat(constraintViolations.size()).isEqualTo(5);
	}
	
	@Test
	void shouldNotCreatecocineroBadPatterns() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Cocinero cocinero= new Cocinero();
		cocinero.setName("Pedro");
		cocinero.setApellido("cocinero");
		cocinero.setContrasena("abc123");
		cocinero.setGmail("jmtr");
		Validator validator = createValidator();
		Set<ConstraintViolation<Cocinero>> constraintViolations =     
				validator.validate(cocinero);
		assertThat(constraintViolations.size()).isEqualTo(2);
	}
	
	@Test
	void shouldNotCreateCamareroShorterPhoneAndPass() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		Cocinero cocinero= new Cocinero();
		cocinero.setName("Pedro");
		cocinero.setApellido("cocinero");
		cocinero.setUsuario("pedrito");
		cocinero.setContrasena("");
		cocinero.setTelefono("");
		cocinero.setGmail("jmtr@gmail.com");
		BindingResult errors = new BeanPropertyBindingResult(cocinero, "");
		ValidationUtils.invokeValidator(new CocineroValidator(), cocinero, errors);
		assertThat(errors.getAllErrors().size()).isEqualTo(3);
		Validator validator = createValidator();
		Set<ConstraintViolation<Cocinero>> constraintViolations =     
				validator.validate(cocinero);
		assertThat(constraintViolations.size()).isEqualTo(3);
	}

	
}
