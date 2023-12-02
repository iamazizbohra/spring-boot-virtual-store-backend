package com.coedmaster.vstore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusDto {
	
	@NotNull
	private boolean enabled;
	
}
