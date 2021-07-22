package org.springframework.samples.foorder.validators;

import org.springframework.samples.foorder.model.Propietario;
import org.springframework.samples.foorder.service.AuthoritiesService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PropietarioValidator implements Validator{
	private AuthoritiesService authoritiesService;

	public PropietarioValidator(AuthoritiesService authoritiesService) {
		super();
		this.authoritiesService = authoritiesService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Propietario.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
	
		Propietario propietario = (Propietario) target;
		
			if (authoritiesService.findAllUsernames().contains(propietario.getUsuario()) ){
				errors.rejectValue("usuario", "este usuario ya está en uso", "este usuario ya está en uso");
			
		}
	}
}
