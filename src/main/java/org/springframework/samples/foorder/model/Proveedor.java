package org.springframework.samples.foorder.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "proveedor")
public class Proveedor extends NamedEntity{
    
    @Size(min = 3, max = 50)
    @Email
	@Column(name = "gmail")
	private String gmail;
    
    //Borrado logico
    @Column(name = "activo",columnDefinition = "number(1,0) default 1")
    private Boolean activo;
    
    @Size(min = 3, max = 50)
	@Column(name = "telefono")
	private String telefono;
    
	//para el cascada
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "proveedor")
	private Set<Producto> Productos;
}
