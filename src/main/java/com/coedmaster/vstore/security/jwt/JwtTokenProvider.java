package com.coedmaster.vstore.security.jwt;

import org.springframework.security.core.Authentication;

public interface JwtTokenProvider {
	String generateToken(Authentication authentication);

	String getUsername(String token);

	boolean validateToken(String token);
}
