package org.springframework.samples.foorder.validators;


import org.springframework.samples.foorder.model.Camarero;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CamareroValidator implements Validator{
	public CamareroValidator() {
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Camarero.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
	
		
		Camarero camarero = (Camarero) target;

		String pattern = "^[a-z0-9!#$%&'+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'+/=?^_`{|}~-]+)@(?:[a-z0-9](?:[a-z0-9-][a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$" ;
		// int[] miArray = new int[] {0,1,2,3,4,5,6,7,8,9};
		String patterncontra= "^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,16}$";
		
		
		if (camarero.getApellido().length()<3||camarero.getApellido().length()>50  ){
			errors.rejectValue("apellido", "este apellido no tiene una longitud valida","este apellido no tiene una longitud valida");
		}

		if (camarero.getName().length()<3||camarero.getName().length()>50  ){
			errors.rejectValue("name", "este nombre no tiene una longitud valida","este nombre no tiene una longitud valida");
		}

		if (camarero.getTelefono().length()<9||camarero.getTelefono().length()>12  ){
			errors.rejectValue("telefono", "este Telefono no tiene una longitud valida","este Telefono no tiene una longitud valida");
		}

		if (camarero.getUsuario().length()<3||camarero.getUsuario().length()>50  ){
			errors.rejectValue("usuario", "este usuario no tiene una longitud valida","este usuario no tiene una longitud valida");
		}

		if (camarero.getContrasena().length()<3||camarero.getContrasena().length()>50  ){
			errors.rejectValue("contrasena", "este contrasena no tiene una longitud valida","este contrasena no tiene una longitud valida");
		}

		if (!camarero.getContrasena().matches(patterncontra)){
			errors.rejectValue("contrasena", "la contrasena debe tener letras y numeros","la contrasena debe tener letras mayusculas, minusculas, numeros y entre 8 y 16 caracteres");
		}
		
		if (!camarero.getGmail().toLowerCase().matches(pattern)){
			errors.rejectValue("gmail", "este gmail no es valido","este gmail no es valido");
		}
	}
}