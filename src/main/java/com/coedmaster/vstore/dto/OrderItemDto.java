package com.coedmaster.vstore.dto;

import lombok.Data;

@Data
public class OrderItemDto {
	
	private Long id;

	private Long orderId;

	private Long categoryId;

	private Long productId;

	private String name;

	private String image;

	private Integer price;

	private Integer quantity;
	
}
