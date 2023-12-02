package com.coedmaster.vstore.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAccountDto extends UpdateUserDto {
	
	private String verificationCode;
	
}
