package org.springframework.samples.foorder.web;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.foorder.model.Plato;
import org.springframework.samples.foorder.service.PlatoService;
import org.springframework.stereotype.Component;

@Component
public class PlatoFormatter implements Formatter<Plato>{
	
	private PlatoService platoService;
	
	@Autowired
	public PlatoFormatter(PlatoService platoService) {
		super();
		this.platoService = platoService;
	}

	@Override
	public String print(Plato plato, Locale locale) {
		return plato.getName();
	}

	@Override
	public Plato parse(String text, Locale locale) throws ParseException {
		Collection<Plato> encuentraPlato = this.platoService.findAll();
		for (Plato p : encuentraPlato) {
			if (p.getName().equals(text)) {
				return p;
			}
		}
		throw new ParseException("tipo no encontrado: " + text, 0);
	}
}
