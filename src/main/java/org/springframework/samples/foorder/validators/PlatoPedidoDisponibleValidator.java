package org.springframework.samples.foorder.validators;

import java.util.Collection;
import java.util.Iterator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.samples.foorder.model.IngredientePedido;
import org.springframework.samples.foorder.model.PlatoPedido;

public class PlatoPedidoDisponibleValidator implements ConstraintValidator<ValidatePlatoPedidoDisponible, PlatoPedido>{

	@Override
	public boolean isValid(PlatoPedido platoPedido, ConstraintValidatorContext context) {
		Boolean res = false;
		Collection<IngredientePedido> lista = platoPedido.getIngredientesPedidos();
		Iterator<IngredientePedido> iterator = null;
		try {
			iterator = lista.iterator();
			while (iterator.hasNext()) {
			    IngredientePedido ing = iterator.next();
			    if((ing.getIngrediente().getProducto().getCantAct() >= ing.getCantidadPedida())||(platoPedido.getEstadoplato().getId().equals(3))||(platoPedido.getEstadoplato().getId().equals(1))) {
			    	res = true;
			    }
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return res;
	}
}
