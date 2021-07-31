package org.springframework.samples.foorder.validators;

import org.springframework.samples.foorder.model.Manager;
import org.springframework.samples.foorder.service.AuthoritiesService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ManagerValidator implements Validator{
	private AuthoritiesService authoritiesService;

	public ManagerValidator(AuthoritiesService authoritiesService) {
		super();
		this.authoritiesService = authoritiesService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Manager.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		Manager manager = (Manager) target;
		String pattern = "^[a-z0-9!#$%&'+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'+/=?^_`{|}~-]+)@(?:[a-z0-9](?:[a-z0-9-][a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$" ;
		// int[] miArray = new int[] {0,1,2,3,4,5,6,7,8,9};
		String patterncontra= "^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,16}$";
		String patternN= "^[0-9].+";
		String patternletras= "^[a-zA-Z].+";

		if (manager.getApellido().length()<3||manager.getApellido().length()>50  ){
			errors.rejectValue("apellido", "este apellido no tiene una longitud valida","este apellido no tiene una longitud valida");
		}

		if (manager.getName().length()<3||manager.getName().length()>50  ){
			errors.rejectValue("name", "este nombre no tiene una longitud valida","este nombre no tiene una longitud valida");
		}

		if (manager.getTelefono().length()<9||manager.getTelefono().length()>12  ){
			errors.rejectValue("telefono", "este Telefono no tiene una longitud valida","este Telefono no tiene una longitud valida");
		}

		if (manager.getUsuario().length()<3||manager.getApellido().length()>50  ){
			errors.rejectValue("usuario", "este usuario no tiene una longitud valida","este usuario no tiene una longitud valida");
		}

		if (manager.getContrasena().length()<3||manager.getApellido().length()>50  ){
			errors.rejectValue("contrasena", "este contrasena no tiene una longitud valida","este contrasena no tiene una longitud valida");
		}

		if (!manager.getContrasena().matches(patterncontra)){
			errors.rejectValue("contrasena", "la contraseña debe tener letras y números","la contraseña debe tener letras mayusculas, minusculas, números y entre 8 y 16 caracteres");
		}
		if (!manager.getGmail().toLowerCase().matches(pattern)){
			errors.rejectValue("gmail", "este gmail no es valido","este gmail no es valido");
		}

		if (authoritiesService.findAllUsernames().contains(manager.getUsuario())){
			errors.rejectValue("usuario", "este usuario ya está en uso", "este usuario ya está en uso");
		}
	}

}
