package com.coedmaster.vstore.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class BannerDto {
	private Long id;

	@NotEmpty
	private String title;

	private String image;

	private Short sortOrder;

	private boolean enabled;
}
