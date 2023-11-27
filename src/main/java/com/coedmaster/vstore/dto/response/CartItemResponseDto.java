package com.coedmaster.vstore.dto.response;

import lombok.Data;

@Data
public class CartItemResponseDto {
	private Long id;

	private Long cartId;

	private Long productId;

	private String name;

	private String image;

	private Integer price;

	private Integer oldPrice;

	private Integer quantity;
}
