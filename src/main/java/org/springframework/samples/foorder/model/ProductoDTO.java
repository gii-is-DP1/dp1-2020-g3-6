package org.springframework.samples.foorder.model;

import javax.persistence.Table;
import javax.validation.constraints.Min;
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
	@NotBlank(message = "selecciona un proveedor")
	private String proveedor;
	private Integer id;
	@NotBlank
	private String name;
	@NotNull
	@Min(0)
	private Double cantMin;
	@NotNull
	@Min(0)
	private Double cantAct;
	@NotNull
	@Min(0)
	private Double cantMax;
}
