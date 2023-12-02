package com.coedmaster.vstore.service.contract;

import java.util.List;

import com.coedmaster.vstore.domain.AuthAccessToken;

public interface IAuthAccessTokenService {
	List<AuthAccessToken> geTokens(Long userId);

	boolean isTokenExists(String token);
}
