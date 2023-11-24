package com.coedmaster.vstore.dto.response;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDto {
	private LocalDateTime timestamp;
	private int status;
	private HttpStatus error;
	private String stackTrace;
	private String message;
	private String path;

}
