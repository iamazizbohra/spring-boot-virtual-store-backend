package com.coedmaster.vstore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDto {
	
	@NotEmpty
	private String firstName;

	@NotEmpty
	private String lastName;

	@NotEmpty
	@Size(min = 10, max = 10)
	private String mobile;

	@NotEmpty
	@Size(min = 8, max = 64)
	private String password;

	@Email
	private String email;

	@NotEmpty
	private String gender;
}
