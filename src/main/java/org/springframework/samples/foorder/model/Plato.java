package org.springframework.samples.foorder.model;


import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.format.annotation.NumberFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "platos")
public class Plato extends NamedEntity{
	
	@NotNull
	@Positive
	@NumberFormat
	@Column(name = "precio")
	private Double precio;
	
	@Column(name = "disponible")
	private Boolean disponible;
	
	@OneToMany(cascade = CascadeType.REMOVE,mappedBy = "plato")
	private Set<Ingrediente> ingredientes;
	
}
