package com.coedmaster.vstore.service;

import com.coedmaster.vstore.dto.AuthenticationDto;

public interface AuthenticationService {
	String authenticate(AuthenticationDto payload);

	String generateToken();
}
