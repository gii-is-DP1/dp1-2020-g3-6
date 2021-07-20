package org.springframework.samples.petclinic.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true,doNotUseGetters = true)
@Entity
@Table(name = "producto")
public class Producto extends NamedEntity{
	
	@ManyToOne
	@JoinColumn(name = "tipo_producto")
	private TipoProducto tipoProducto;

	@NotNull
	@Column(name = "cantidad_minima")
	private Double cantMin;

	@NotNull
	@Column(name = "cantidad_actual")
	private Double cantAct;
	
	@NotNull
	@Column(name = "cantidad_maxima")
	private Double cantMax;
	
	@ManyToOne
	@JoinColumn(name = "proveedor_id")
	private Proveedor proveedor;
	
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "producto")
	private Set<LineaPedido> lineasPedidas;
	
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "producto")
	private Set<Ingrediente> ingredientes;

}
