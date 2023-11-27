package com.coedmaster.vstore.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemRequestDto {
	@NotNull
	private Long productId;

	@NotNull
	@Min(value = 1)
	private Integer quantity;
}
