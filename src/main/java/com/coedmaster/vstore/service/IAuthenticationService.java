package com.coedmaster.vstore.service;

import com.coedmaster.vstore.dto.AuthenticationDto;

public interface IAuthenticationService {
	String authenticate(AuthenticationDto payload);

	String generateToken();
}
