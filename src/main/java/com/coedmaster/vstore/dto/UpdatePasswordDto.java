package com.coedmaster.vstore.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdatePasswordDto {
	@NotEmpty
	String currentPassword;

	@NotEmpty
	String newPassword;
}
