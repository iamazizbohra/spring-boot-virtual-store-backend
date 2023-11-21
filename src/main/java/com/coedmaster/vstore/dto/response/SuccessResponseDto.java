package com.coedmaster.vstore.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuccessResponseDto {
	private LocalDateTime timestamp;
	private int status;
	private String message;
	private Object data;
	private String path;

}
