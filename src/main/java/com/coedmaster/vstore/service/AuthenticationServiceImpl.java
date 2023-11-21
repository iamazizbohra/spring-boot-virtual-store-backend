package com.coedmaster.vstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.dto.request.LoginRequestDto;
import com.coedmaster.vstore.security.jwt.JwtTokenProvider;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Override
	public String authenticate(LoginRequestDto payload) {	
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(payload.getUsername(), payload.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return tokenProvider.generateToken(authentication);
	}

}
