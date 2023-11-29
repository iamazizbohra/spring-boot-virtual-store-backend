package com.coedmaster.vstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.dto.AuthenticateDto;
import com.coedmaster.vstore.model.IUserDetails;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.respository.UserRepository;
import com.coedmaster.vstore.security.provider.IJwtTokenProvider;

@Service
public class AuthenticationService implements IAuthenticationService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	IJwtTokenProvider jwtTokenProvider;

	@Override
	public Authentication authenticate(AuthenticateDto payload) {
		return authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(payload.getUsername(), payload.getPassword()));
	}

	@Override
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	@Override
	public void setAuthentication(Authentication authentication) {
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Override
	public IUserDetails getAuthenticatedUserDetails(Authentication authentication) {
		return (IUserDetails) authentication.getPrincipal();
	}

	@Override
	public User getAuthenticatedUser(Authentication authentication) {
		IUserDetails userDetails = getAuthenticatedUserDetails(authentication);

		return userRepository.findById(userDetails.getId())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

}
