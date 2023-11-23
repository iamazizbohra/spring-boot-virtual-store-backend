package com.coedmaster.vstore.dto.response;

import lombok.Data;

@Data
public class CategoryResponseDto {

	private Long id;

	private String title;

	private String image;

	private boolean enabled;
}
