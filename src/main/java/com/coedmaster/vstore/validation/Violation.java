package com.coedmaster.vstore.validation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Violation {
	private final String fieldName;

	private final String message;
}
