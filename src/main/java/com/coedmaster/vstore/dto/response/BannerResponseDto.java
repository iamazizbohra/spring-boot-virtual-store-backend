package com.coedmaster.vstore.dto.response;

import lombok.Data;

@Data
public class BannerResponseDto {
	
	private Long id;
	
	private String title;

	private String image;
	
	private Short sortOrder;
	
	private boolean enabled;
}
