package com.coedmaster.vstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.domain.AuthAccessToken;
import com.coedmaster.vstore.respository.AuthAccessTokenRepository;
import com.coedmaster.vstore.service.contract.IAuthAccessTokenService;

@Service
public class AuthAccessTokenService implements IAuthAccessTokenService {

	@Autowired
	private AuthAccessTokenRepository authAccessTokenRepository;

	@Override
	public List<AuthAccessToken> geTokens(Long userId) {
		return authAccessTokenRepository.findByUserId(userId);
	}

	@Override
	public boolean isTokenExists(String token) {
		Optional<AuthAccessToken> authAccessTokenOptional = authAccessTokenRepository.findByToken(token);

		if (authAccessTokenOptional.isEmpty()) {
			return false;
		}

		return true;
	}

}
