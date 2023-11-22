package com.coedmaster.vstore.dto.response;

import com.coedmaster.vstore.dto.JwtTokenDto;
import com.coedmaster.vstore.dto.UserDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountResponseDto {
	private UserDto user;
	private JwtTokenDto jwt;
}
