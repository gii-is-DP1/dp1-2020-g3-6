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
		String pattern = "^[a-z0-9!#$%&'+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'+/=?^_`{|}~-]+)@(?:[a-z0-9](?:[a-z0-9-][a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$" ;
		// int[] miArray = new int[] {0,1,2,3,4,5,6,7,8,9};
		String patterncontra= "^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,16}$";
		String patternN= "^[0-9].+";
		String patternletras= "^[a-zA-Z].+";

		if (propietario.getApellido().length()<3||propietario.getApellido().length()>50  ){
			errors.rejectValue("apellido", "este apellido no tiene una longitud valida","este apellido no tiene una longitud valida");
		}

		if (propietario.getName().length()<3||propietario.getName().length()>50  ){
			errors.rejectValue("name", "este nombre no tiene una longitud valida","este nombre no tiene una longitud valida");
		}

		if (propietario.getTelefono().length()<9||propietario.getTelefono().length()>12  ){
			errors.rejectValue("telefono", "este Telefono no tiene una longitud valida","este Telefono no tiene una longitud valida");
		}

		if (propietario.getUsuario().length()<3||propietario.getApellido().length()>50  ){
			errors.rejectValue("usuario", "este usuario no tiene una longitud valida","este usuario no tiene una longitud valida");
		}

		if (propietario.getContrasena().length()<3||propietario.getApellido().length()>50  ){
			errors.rejectValue("contrasena", "este contrasena no tiene una longitud valida","este contrasena no tiene una longitud valida");
		}

		//	            if (propietario.getContrasena().matches(patternN)||propietario.getContrasena().matches(patternletras)){
		//	                errors.rejectValue("contrasena", "la contraseña debe tener letras y números","la contraseña debe tener letras y números");
		//	            }
		if (!propietario.getContrasena().matches(patterncontra)){
			errors.rejectValue("contrasena", "la contraseña debe tener letras y números","la contraseña debe tener letras mayusculas, minusculas, números y entre 8 y 16 caracteres");
		}
		if (!propietario.getGmail().toLowerCase().matches(pattern)){
			errors.rejectValue("gmail", "este gmail no es valido","este gmail no es valido");
		}

		if (authoritiesService.findAllUsernames().contains(propietario.getUsuario())){
			errors.rejectValue("usuario", "este usuario ya está en uso", "este usuario ya está en uso");
		}
	}

}