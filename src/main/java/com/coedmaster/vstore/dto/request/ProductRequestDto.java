package com.coedmaster.vstore.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRequestDto {
	@NotNull
	private Long categoryId;

	@NotEmpty
	private String name;

	private String description;

	private String image;

	@NotNull
	private Integer price;

	private Integer oldPrice;

	@NotNull
	private Integer quantity;

}
