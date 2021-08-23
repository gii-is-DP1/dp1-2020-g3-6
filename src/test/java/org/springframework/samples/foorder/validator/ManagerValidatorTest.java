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
import org.springframework.samples.foorder.model.Manager;
import org.springframework.samples.foorder.validators.ManagerValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@RunWith(MockitoJUnitRunner.class)
public class ManagerValidatorTest {
	
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = 
		new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	@Test
	void shouldNotCreatemanagerWithWithLowerLenght() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Manager manager= new Manager();
		manager.setName("pe");
		manager.setApellido("pe");
		manager.setTelefono("32");
		manager.setUsuario("pe");
		manager.setContrasena("ab");
		Validator validator = createValidator();
		Set<ConstraintViolation<Manager>> constraintViolations =     
				validator.validate(manager);
		assertThat(constraintViolations.size()).isEqualTo(5);
	}
	
	@Test
	void shouldNotCreatemanagerWithWithLongerLenght() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		String muchoTexto= "qwertyuioopasdfghjklnzxcvbnmqwertyuiopasdfghjklnzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuioasdfghjklzxcvbnmqwertyuiopqwertyuiopqwertyuiopqwertyuioqqwertyuiuytrewwertyuiuytrewq";
		Manager manager= new Manager();
		manager.setName(muchoTexto);
		manager.setApellido(muchoTexto);
		manager.setTelefono("1234567890123567");
		manager.setUsuario(muchoTexto);
		manager.setContrasena(muchoTexto);
		Validator validator = createValidator();
		Set<ConstraintViolation<Manager>> constraintViolations =     
				validator.validate(manager);
		assertThat(constraintViolations.size()).isEqualTo(5);
	}
	
	@Test
	void shouldNotCreatemanagerBadPatterns() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Manager manager= new Manager();
		manager.setName("Pedro");
		manager.setApellido("manager");
		manager.setContrasena("abc123");
		manager.setGmail("jmtr");
		Validator validator = createValidator();
		Set<ConstraintViolation<Manager>> constraintViolations =     
				validator.validate(manager);
		assertThat(constraintViolations.size()).isEqualTo(2);
	}
	
	@Test
	void shouldNotCreateCamareroShorterPhoneAndPass() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		Manager manager= new Manager();
		manager.setName("Pedro");
		manager.setApellido("manager");
		manager.setUsuario("pedrito");
		manager.setContrasena("");
		manager.setTelefono("");
		manager.setGmail("jmtr@gmail.com");
		BindingResult errors = new BeanPropertyBindingResult(manager, "");
		ValidationUtils.invokeValidator(new ManagerValidator(), manager, errors);
		assertThat(errors.getAllErrors().size()).isEqualTo(3);
		Validator validator = createValidator();
		Set<ConstraintViolation<Manager>> constraintViolations =     
				validator.validate(manager);
		assertThat(constraintViolations.size()).isEqualTo(3);
	}


	
}
