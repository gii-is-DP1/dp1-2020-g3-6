package org.springframework.samples.foorder.validator;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.foorder.model.Comanda;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class NumeroMesaValidator {
	
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = 
		new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	
	@Test
	void shouldNotCreateWithTableGreaterThan20() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		Comanda comanda = new Comanda();
		comanda.setFechaCreado(LocalDateTime.now());
		comanda.setMesa(60);
		Validator validator = createValidator();
		Set<ConstraintViolation<Comanda>> constraintViolations =     
				validator.validate(comanda);
		assertThat(constraintViolations.size()).isEqualTo(1);
		
	}
	
	@Test
	void shouldNotCreateWithTable0OrSmaller() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		Comanda comanda = new Comanda();
		comanda.setFechaCreado(LocalDateTime.now());
		comanda.setMesa(0);
		Validator validator = createValidator();
		Set<ConstraintViolation<Comanda>> constraintViolations =     
				validator.validate(comanda);
		assertThat(constraintViolations.size()).isEqualTo(2);
		
	}
	
	@Test
	void shouldCreateWithTableBTW1And20() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		Comanda comanda = new Comanda();
		comanda.setFechaCreado(LocalDateTime.now());
		comanda.setMesa(10);
		Validator validator = createValidator();
		Set<ConstraintViolation<Comanda>> constraintViolations =     
				validator.validate(comanda);
		assertThat(constraintViolations.size()).isEqualTo(0);
		
	}
}
