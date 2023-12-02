package com.coedmaster.vstore.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountDto extends CreateUserDto {
	
	private String verificationCode;
}
