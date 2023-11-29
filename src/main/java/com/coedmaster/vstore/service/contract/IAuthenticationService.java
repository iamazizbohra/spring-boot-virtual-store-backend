package com.coedmaster.vstore.service.contract;

import org.springframework.security.core.Authentication;

import com.coedmaster.vstore.domain.IUserDetails;
import com.coedmaster.vstore.domain.User;
import com.coedmaster.vstore.dto.AuthenticateDto;

public interface IAuthenticationService {

	Authentication authenticate(AuthenticateDto payload);

	Authentication getAuthentication();

	void setAuthentication(Authentication authentication);

	IUserDetails getAuthenticatedUserDetails(Authentication authentication);

	User getAuthenticatedUser(Authentication authentication);

}
