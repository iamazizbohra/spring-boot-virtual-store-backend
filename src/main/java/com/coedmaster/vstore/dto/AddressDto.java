package com.coedmaster.vstore.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AddressDto {
	private Long id;

	@NotEmpty
	private String title;

	@NotEmpty
	private String name;

	@NotEmpty
	private String mobile;

	@NotEmpty
	private String state;

	@NotEmpty
	private String city;

	@NotEmpty
	private String pincode;

	@NotEmpty
	private String line1;

	private String line2;

	private String landmark;

	private boolean isDefault;
}
