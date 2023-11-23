package com.coedmaster.vstore.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class BannerRequestDto {
	@NotEmpty
	private String title;

	private String image;
}