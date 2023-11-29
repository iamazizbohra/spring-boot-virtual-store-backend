package com.coedmaster.vstore.service;

import org.springframework.security.core.Authentication;

import com.coedmaster.vstore.dto.AuthenticateDto;
import com.coedmaster.vstore.model.IUserDetails;
import com.coedmaster.vstore.model.User;

public interface IAuthenticationService {
	Authentication getAuthentication();

	void setAuthentication(Authentication authentication);

	IUserDetails getAuthenticatedUserDetails(Authentication authentication);

	User getAuthenticatedUser(Authentication authentication);

	Authentication authenticate(AuthenticateDto payload);
}
