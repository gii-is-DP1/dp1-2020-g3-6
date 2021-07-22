package org.springframework.samples.petclinic.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Camarero extends Empleado{
	
	public Camarero(Camarero cam) {
	}

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "camarero")
	private Set<Comanda> comandas;
	
}
