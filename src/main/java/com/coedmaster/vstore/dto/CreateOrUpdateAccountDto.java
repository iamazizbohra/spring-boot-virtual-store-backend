package com.coedmaster.vstore.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateOrUpdateAccountDto {
	@NotEmpty
	private String firstName;

	@NotEmpty
	private String lastName;

	@NotEmpty
	private String mobile;

	@NotEmpty
	private String password;

	private String email;

	@NotEmpty
	private String gender;
}
