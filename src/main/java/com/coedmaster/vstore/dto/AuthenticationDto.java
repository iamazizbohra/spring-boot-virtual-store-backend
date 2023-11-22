package com.coedmaster.vstore.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AuthenticationDto {
	@NotEmpty
	private String username;
	
	@NotEmpty
	private String password;
}
