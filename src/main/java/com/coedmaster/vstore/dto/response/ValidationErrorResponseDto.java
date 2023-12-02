package com.coedmaster.vstore.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.coedmaster.vstore.domain.validation.Violation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidationErrorResponseDto {
	
	private LocalDateTime timestamp;
	
	private int status;
	
	private HttpStatus error;
	
	private String message;
	
	private String path;
	
	private List<Violation> violations;

}
