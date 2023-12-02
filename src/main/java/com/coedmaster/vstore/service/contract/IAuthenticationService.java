package com.coedmaster.vstore.service.contract;

import org.springframework.security.core.Authentication;

import com.coedmaster.vstore.domain.AuthAccessToken;
import com.coedmaster.vstore.domain.User;
import com.coedmaster.vstore.domain.contract.IUserDetails;

public interface IAuthenticationService {

	Authentication authenticate(String username, String password);

	Authentication getAuthentication();

	IUserDetails getAuthenticatedUserDetails(Authentication authentication);

	User getAuthenticatedUser(Authentication authentication);

	AuthAccessToken generateToken(User user);

	String getSubjectFromToken(String token);

	boolean validateToken(String token);

	boolean isTokenExists(String token);

}
