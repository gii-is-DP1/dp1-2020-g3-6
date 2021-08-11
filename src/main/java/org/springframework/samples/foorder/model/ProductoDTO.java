package org.springframework.samples.foorder.model;

import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "productodto")
public class ProductoDTO {
	@NotBlank
	private String tipoproductodto;
	@NotBlank
	private String proveedor;
	private Integer id;
	@NotBlank
	private String name;
	@NotNull
	private double cantMin;
	@NotNull
	private double cantAct;
	@NotNull
	private double cantMax;
}
