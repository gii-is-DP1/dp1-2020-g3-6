package org.springframework.samples.foorder.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Camarero extends Empleado{

    public Camarero(Camarero cam) {
    }
	
//	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "camarero")
//	private Set<Comanda> comandas;
	
}
