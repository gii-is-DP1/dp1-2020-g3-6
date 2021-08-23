package org.springframework.samples.foorder.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = (NumeroMesaValidator.class))
public @interface NumeroMesaConstraint {
	String message() default "El numero de mesa debe estar entre 1 y 20";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
