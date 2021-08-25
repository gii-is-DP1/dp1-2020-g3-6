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
import org.springframework.samples.foorder.model.Propietario;
import org.springframework.samples.foorder.validators.PropietarioValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@RunWith(MockitoJUnitRunner.class)
public class PropietarioValidatorTest {
	
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = 
		new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	@Test
	void shouldNotCreatepropietarioWithWithLowerLenght() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Propietario propietario= new Propietario();
		propietario.setName("pe");
		propietario.setApellido("pe");
		propietario.setTelefono("32");
		propietario.setUsuario("pe");
		propietario.setContrasena("ab");
		Validator validator = createValidator();
		Set<ConstraintViolation<Propietario>> constraintViolations =     
				validator.validate(propietario);
		assertThat(constraintViolations.size()).isEqualTo(5);
	}
	
	@Test
	void shouldNotCreatepropietarioWithWithLongerLenght() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		String muchoTexto= "qwertyuioopasdfghjklnzxcvbnmqwertyuiopasdfghjklnzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuioasdfghjklzxcvbnmqwertyuiopqwertyuiopqwertyuiopqwertyuioqqwertyuiuytrewwertyuiuytrewq";
		Propietario propietario= new Propietario();
		propietario.setName(muchoTexto);
		propietario.setApellido(muchoTexto);
		propietario.setTelefono("1234567890123567");
		propietario.setUsuario(muchoTexto);
		propietario.setContrasena(muchoTexto);
		Validator validator = createValidator();
		Set<ConstraintViolation<Propietario>> constraintViolations =     
				validator.validate(propietario);
		assertThat(constraintViolations.size()).isEqualTo(5);
	}
	
	@Test
	void shouldNotCreatepropietarioBadPatterns() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Propietario propietario= new Propietario();
		propietario.setName("Pedro");
		propietario.setApellido("propietario");
		propietario.setContrasena("abc123");
		propietario.setGmail("jmtr");
		Validator validator = createValidator();
		Set<ConstraintViolation<Propietario>> constraintViolations =     
				validator.validate(propietario);
		assertThat(constraintViolations.size()).isEqualTo(2);
	}
	
	@Test
	void shouldNotCreateCamareroShorterPhoneAndPass() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		Propietario propietario= new Propietario();
		propietario.setName("Pedro");
		propietario.setApellido("propietario");
		propietario.setUsuario("pedrito");
		propietario.setContrasena("");
		propietario.setTelefono("");
		propietario.setGmail("jmtr@gmail.com");
		BindingResult errors = new BeanPropertyBindingResult(propietario, "");
		ValidationUtils.invokeValidator(new PropietarioValidator(), propietario, errors);
		assertThat(errors.getAllErrors().size()).isEqualTo(3);
		Validator validator = createValidator();
		Set<ConstraintViolation<Propietario>> constraintViolations =     
				validator.validate(propietario);
		assertThat(constraintViolations.size()).isEqualTo(3);
	}

	
}
