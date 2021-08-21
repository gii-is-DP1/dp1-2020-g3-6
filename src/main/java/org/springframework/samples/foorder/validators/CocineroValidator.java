package org.springframework.samples.foorder.validators;

import org.springframework.samples.foorder.model.Cocinero;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CocineroValidator implements Validator{


	public CocineroValidator() {
		super();
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Cocinero.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		Cocinero cocinero = (Cocinero) target;
		String pattern = "^[a-z0-9!#$%&'+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'+/=?^_`{|}~-]+)@(?:[a-z0-9](?:[a-z0-9-][a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$" ;
		// int[] miArray = new int[] {0,1,2,3,4,5,6,7,8,9};
		String patterncontra= "^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{3,50}$";

		if (cocinero.getApellido().length()<3||cocinero.getApellido().length()>50  ){
			errors.rejectValue("apellido", "este apellido no tiene una longitud valida","este apellido no tiene una longitud valida");
		}

		if (cocinero.getName().length()<3||cocinero.getName().length()>50  ){
			errors.rejectValue("name", "este nombre no tiene una longitud valida","este nombre no tiene una longitud valida");
		}

		if (cocinero.getTelefono().length()<9||cocinero.getTelefono().length()>12  ){
			errors.rejectValue("telefono", "este Telefono no tiene una longitud valida","este Telefono no tiene una longitud valida");
		}

		if (cocinero.getUsuario().length()<3||cocinero.getUsuario().length()>50  ){
			errors.rejectValue("usuario", "este usuario no tiene una longitud valida","este usuario no tiene una longitud valida");
		}

		if (cocinero.getContrasena().length()<3||cocinero.getContrasena().length()>50  ){
			errors.rejectValue("contrasena", "este contrasena no tiene una longitud valida","este contrasena no tiene una longitud valida");
		}

		if (!cocinero.getContrasena().matches(patterncontra)){
			errors.rejectValue("contrasena", "la contrasena debe tener letras y números","la contrasena debe tener letras mayusculas, minusculas, números y entre 8 y 16 caracteres");
		}
		
		if (!cocinero.getGmail().toLowerCase().matches(pattern)){
			errors.rejectValue("gmail", "este gmail no es valido","este gmail no es valido");
		}

	}

}

