package com.coedmaster.vstore.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccessTokenDto {

	@Builder.Default
	private String type = "Bearer";

	private String token;

}
