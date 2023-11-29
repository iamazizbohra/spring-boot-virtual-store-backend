package com.coedmaster.vstore.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coedmaster.vstore.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByMobile(String mobile);
}
