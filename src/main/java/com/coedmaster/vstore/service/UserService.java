package com.coedmaster.vstore.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.domain.User;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.respository.UserRepository;
import com.coedmaster.vstore.service.contract.IUserService;

@Service
public class UserService implements IUserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User getUser(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
	}

	@Override
	public User getUserByUuid(String uuid) {
		return userRepository.findByUuid(UUID.fromString(uuid)).orElseThrow(() -> new EntityNotFoundException("User not found"));
	}

	@Override
	public User getUserByMobile(String mobile) {
		return userRepository.findByMobile(mobile).orElseThrow(() -> new EntityNotFoundException("User not found"));
	}

}
