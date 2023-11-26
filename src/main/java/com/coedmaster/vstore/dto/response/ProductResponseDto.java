package com.coedmaster.vstore.dto.response;

import lombok.Data;

@Data
public class ProductResponseDto {
	private Long id;
	
	private Long categoryId;

	private String name;

	private String description;

	private String image;

	private Integer price;

	private Integer oldPrice;

	private Integer quantity;

	private boolean enabled;
}
