package com.coedmaster.vstore.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdatePasswordDto {
	
	@NotEmpty
	String currentPassword;

	@NotEmpty
	String newPassword;
	
}
