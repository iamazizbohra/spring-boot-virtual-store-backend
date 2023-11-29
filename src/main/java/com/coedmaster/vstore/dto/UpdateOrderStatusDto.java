package com.coedmaster.vstore.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateOrderStatusDto {
	@NotEmpty
	private String status;
}
