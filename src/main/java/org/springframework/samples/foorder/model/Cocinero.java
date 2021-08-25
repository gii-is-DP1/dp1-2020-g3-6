package org.springframework.samples.foorder.model;

import javax.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Cocinero extends Empleado{
	public Cocinero(Cocinero coci) {
    }
	
}

