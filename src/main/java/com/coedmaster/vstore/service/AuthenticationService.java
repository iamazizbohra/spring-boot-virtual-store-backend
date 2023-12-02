package com.coedmaster.vstore.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.config.properties.JwtCredentials;
import com.coedmaster.vstore.domain.AuthAccessToken;
import com.coedmaster.vstore.domain.User;
import com.coedmaster.vstore.domain.contract.IUserDetails;
import com.coedmaster.vstore.respository.AuthAccessTokenRepository;
import com.coedmaster.vstore.service.contract.IAuthenticationService;
import com.coedmaster.vstore.service.contract.IUserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenticationService implements IAuthenticationService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private IUserService userService;

	@Autowired
	private AuthAccessTokenRepository authAccessTokenRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private JwtCredentials jwtCredentials;

	@Value("${spring.application.name}")
	private String appName;

	@Override
	public Authentication authenticate(String username, String password) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return authentication;
	}

	@Override
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	@Override
	public IUserDetails getAuthenticatedUserDetails(Authentication authentication) {
		return (IUserDetails) authentication.getPrincipal();
	}

	@Override
	public User getAuthenticatedUser(Authentication authentication) {
		IUserDetails userDetails = getAuthenticatedUserDetails(authentication);

		return userService.getUser(userDetails.getId());
	}

	@Override
	public boolean verifyPassword(User user, String password) {
		return passwordEncoder.matches(password, user.getPassword());
	}

	@Override
	public AuthAccessToken generateToken(User user) {
		Date today = new Date();
		Date expiration = DateUtils.addMilliseconds(today, jwtCredentials.getTtl());
		LocalDateTime expiresAt = LocalDateTime.ofInstant(today.toInstant(), ZoneId.systemDefault());

		String token = Jwts.builder().issuer(appName).subject(user.getUuid().toString()).issuedAt(today)
				.expiration(expiration).id(UUID.randomUUID().toString()).signWith(key()).compact();

		AuthAccessToken accessToken = new AuthAccessToken();
		accessToken.setUser(user);
		accessToken.setName("Auth Token");
		accessToken.setToken(token);
		accessToken.setExpiresAt(expiresAt);

		return authAccessTokenRepository.save(accessToken);
	}

	@Override
	public String getSubjectFromToken(String token) {
		Jws<Claims> parsed = null;

		try {
			parsed = Jwts.parser().verifyWith(key()).build().parseSignedClaims(token);
		} catch (JwtException e) {
			log.error(e.getMessage());
		}

		return parsed.getPayload().getSubject();
	}

	@Override
	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith(key()).build().parseSignedClaims(token);
			return true;
		} catch (JwtException e) {
			log.error(e.getMessage());
		}

		return false;
	}

	@Override
	public boolean isTokenExists(String token) {
		Optional<AuthAccessToken> authAccessTokenOptional = authAccessTokenRepository.findByToken(token);

		if (authAccessTokenOptional.isEmpty()) {
			return false;
		}

		return true;
	}

	@Override
	public void deleteAllTokens(User user) {
		List<AuthAccessToken> authAccessTokens = authAccessTokenRepository.findAllByUserId(user.getId());

		for (AuthAccessToken authAccessToken : authAccessTokens) {
			authAccessTokenRepository.deleteById(authAccessToken.getId());
		}
	}

	private SecretKey key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtCredentials.getSecret()));
	}

}
