package com.coedmaster.vstore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateSortOrderDto {
	
	@NotNull
	private Short sortOrder;
	
}
