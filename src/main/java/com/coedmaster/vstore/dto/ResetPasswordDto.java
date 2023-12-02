package com.coedmaster.vstore.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordDto {
	
	@NotEmpty
	@Size(min = 10, max = 10)
	private String mobile;

	private String verificationCode;

	@NotEmpty
	@Size(min = 8, max = 64)
	private String password;
	
}
