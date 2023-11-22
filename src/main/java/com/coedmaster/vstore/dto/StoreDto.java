package com.coedmaster.vstore.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class StoreDto {
	private Long id;

	@NotEmpty
	private String name;

	@NotEmpty
	private String code;

	private String logo;

	@NotEmpty
	private String mobile;

	@NotEmpty
	private String whatsapp;

	private String email;

	private String latitude;

	private String longitude;

	@NotEmpty
	private String address;
}
