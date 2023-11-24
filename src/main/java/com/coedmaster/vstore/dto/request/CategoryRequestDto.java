package com.coedmaster.vstore.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CategoryRequestDto {
	@NotEmpty
	private String name;

	private String image;
}
