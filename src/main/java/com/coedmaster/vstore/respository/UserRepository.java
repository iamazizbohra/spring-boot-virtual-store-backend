package com.coedmaster.vstore.respository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coedmaster.vstore.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUuid(UUID uuid);

	Optional<User> findByMobile(String mobile);
}
