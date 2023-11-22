package com.coedmaster.vstore.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AccountRequestDto {
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
