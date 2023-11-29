package com.coedmaster.vstore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddCartItemDto {
	@NotNull
	private Long productId;

	@NotNull
	@Min(value = 1)
	private Integer quantity;
}
