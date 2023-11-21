package com.coedmaster.vstore.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtTokenDto {
	@Builder.Default
	private String tokenType = "Bearer";

	private String accessToken;
}
