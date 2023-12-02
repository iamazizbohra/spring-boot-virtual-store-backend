package com.coedmaster.vstore.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coedmaster.vstore.domain.AuthAccessToken;

public interface AuthAccessTokenRepository extends JpaRepository<AuthAccessToken, Long> {
	List<AuthAccessToken> findAllByUserId(Long userId);

	Optional<AuthAccessToken> findByToken(String token);
}
