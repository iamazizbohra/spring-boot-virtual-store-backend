package com.coedmaster.vstore.domain.validation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Violation {
	private final String fieldName;

	private final String message;
}
