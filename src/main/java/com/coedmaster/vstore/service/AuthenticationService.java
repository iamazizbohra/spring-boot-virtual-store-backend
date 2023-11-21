package com.coedmaster.vstore.service;

import com.coedmaster.vstore.dto.request.LoginRequestDto;

public interface AuthenticationService {
	String authenticate(LoginRequestDto payload);
}
