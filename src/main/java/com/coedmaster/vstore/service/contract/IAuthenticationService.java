package com.coedmaster.vstore.service.contract;

import org.springframework.security.core.Authentication;

import com.coedmaster.vstore.model.contract.IUserDetails;
import com.coedmaster.vstore.model.AuthAccessToken;
import com.coedmaster.vstore.model.User;

public interface IAuthenticationService {

	Authentication authenticate(String username, String password);

	Authentication getAuthentication();

	IUserDetails getAuthenticationUserDetails(Authentication authentication);

	User getAuthenticatedUser(Authentication authentication);

	boolean verifyPassword(User user, String password);

	AuthAccessToken generateToken(User user);

	String getSubjectFromToken(String token);

	boolean validateToken(String token);

	boolean isTokenExists(String token);

	void deleteAllTokens(User user);

}
