package org.springframework.samples.foorder.validators;

import java.util.Collection;
import java.util.Iterator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.samples.foorder.model.Comanda;


public class NumeroMesaValidator implements ConstraintValidator<NumeroMesaConstraint, Comanda>{
	
	@Override
	public boolean isValid(Comanda comanda, ConstraintValidatorContext context) {
		Boolean res = true;
		if(comanda.getMesa()==null||comanda.getMesa()<1||comanda.getMesa()>20)
		    res = false;
		return res;
	}

}
