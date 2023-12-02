package com.coedmaster.vstore.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AuthenticateDto {
	
	@NotEmpty
	private String username;
	
	@NotEmpty
	private String password;
	
}
