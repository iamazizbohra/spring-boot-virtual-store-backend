package com.coedmaster.vstore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAccountDto {
	@NotEmpty
	private String firstName;

	@NotEmpty
	private String lastName;

	@NotEmpty
	@Size(min = 10, max = 10)
	private String mobile;

	private String verificationCode;

	@NotEmpty
	@Size(min = 8, max = 64)
	private String password;

	@Email
	private String email;

	@NotEmpty
	private String gender;
}
