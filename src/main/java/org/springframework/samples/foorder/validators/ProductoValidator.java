package org.springframework.samples.foorder.validators;

import org.springframework.samples.foorder.model.ProductoDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Component
public class ProductoValidator implements Validator{
	private static final Log LOG = LogFactory.getLog(ProductoDTO.class);


	@Override
	public boolean supports(Class<?> clazz) {
		return ProductoDTO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
	
		
		ProductoDTO productoDTO = (ProductoDTO) target;

		
		if(StringUtils.isEmpty(productoDTO.getName())) {
			LOG.warn("el nombre esta vacio");
			errors.rejectValue("name","no puede estar vacio" ,"no puede estar vacio");
		}
		if(StringUtils.isEmpty(productoDTO.getTipoproductodto())) {
			LOG.warn("el tipoproductodto esta vacio");
			errors.rejectValue("tipoproductodto","no puede ser nulo" ,"no puede ser nulo");
		}
		if(StringUtils.isEmpty(productoDTO.getProveedor())) {
			LOG.warn("el proveedor esta vacio");
			errors.rejectValue("proveedor", "no puede ser nulo","no puede ser nulo");
		}
		Double cantMax= productoDTO.getCantMax();
		if(productoDTO.getCantMin()!=null&&productoDTO.getCantMax()!=null&&productoDTO.getCantAct()>(cantMax+cantMax*25.0/100)) {
			LOG.warn("actual supera mucho a la maxima");
			errors.rejectValue("cantAct", "cantidad actual supera mucho a la maxima","cantidad actual supera mucho a la maxima");
		}
		
		

	}

}