package com.coedmaster.vstore.security.provider;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.coedmaster.vstore.config.properties.JwtCredentials;
import com.coedmaster.vstore.domain.IUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider implements IJwtTokenProvider {

	private JwtCredentials jwtCredentials;

	public JwtTokenProvider(JwtCredentials jwtCredentials) {
		this.jwtCredentials = jwtCredentials;
	}

	@Override
	public String generateToken(Authentication authentication) {
		IUserDetails userDetails = (IUserDetails) authentication.getPrincipal();

		Date currentDate = new Date();

		Date expireDate = DateUtils.addMilliseconds(currentDate, jwtCredentials.getTtl());

		String jws = Jwts.builder().subject(userDetails.getMobile()).issuedAt(currentDate).expiration(expireDate)
				.id(UUID.randomUUID().toString()).signWith(key()).compact();

		return jws;
	}

	@Override
	public String getUsername(String jws) {
		Jws<Claims> parsed = null;

		try {
			parsed = Jwts.parser().verifyWith(key()).build().parseSignedClaims(jws);
		} catch (JwtException e) {
			log.error(e.getMessage());
		}

		return parsed.getPayload().getSubject();
	}

	@Override
	public boolean validateToken(String jws) {
		try {
			Jwts.parser().verifyWith(key()).build().parseSignedClaims(jws);
			return true;
		} catch (JwtException e) {
			log.error(e.getMessage());
		}

		return false;
	}

	private SecretKey key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtCredentials.getSecret()));
	}

}
