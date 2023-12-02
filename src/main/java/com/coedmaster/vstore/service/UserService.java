package com.coedmaster.vstore.service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.domain.Role;
import com.coedmaster.vstore.domain.User;
import com.coedmaster.vstore.domain.embeddable.FullName;
import com.coedmaster.vstore.dto.CreateUserDto;
import com.coedmaster.vstore.dto.UpdateUserDto;
import com.coedmaster.vstore.enums.Gender;
import com.coedmaster.vstore.enums.UserType;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.exception.UsernameAlreadyTakenException;
import com.coedmaster.vstore.respository.UserRepository;
import com.coedmaster.vstore.service.contract.IUserService;

@Service
public class UserService implements IUserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public User getUser(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
	}

	@Override
	public User getUserByUuid(String uuid) {
		return userRepository.findByUuid(UUID.fromString(uuid))
				.orElseThrow(() -> new EntityNotFoundException("User not found"));
	}

	@Override
	public User getUserByMobile(String mobile) {
		return userRepository.findByMobile(mobile).orElseThrow(() -> new EntityNotFoundException("User not found"));
	}

	@Override
	public boolean isMobileAvailable(String mobile) {
		Optional<User> optionalUser = userRepository.findByMobile(mobile);
		if (!optionalUser.isEmpty()) {
			return false;
		}

		return true;
	}

	@Override
	public boolean isMobileAvailableFor(String mobile, User user) {
		Optional<User> userOptional = userRepository.findByMobile(mobile);
		if (!userOptional.isEmpty()) {
			if (!userOptional.get().getId().equals(user.getId()))
				throw new UsernameAlreadyTakenException("Mobile no is already taken");
		}

		return true;
	}

	@Override
	public User createUser(UserType userType, Role role, CreateUserDto payload) {
		FullName fullName = new FullName();
		fullName.setFirstName(payload.getFirstName());
		fullName.setLastName(payload.getLastName());

		User user = new User();
		user.setUuid(UUID.randomUUID());
		user.setUserType(userType);
		user.setFullName(fullName);
		user.setMobile(payload.getMobile());
		user.setPassword(passwordEncoder.encode(payload.getPassword()));
		user.setEmail(payload.getEmail());
		user.setGender(Gender.valueOf(payload.getGender()));
		user.setRoles(Collections.singletonList(role));
		user.setEnabled(true);

		return userRepository.save(user);
	}

	@Override
	public User updateUser(User user, UpdateUserDto payload) {
		FullName fullName = new FullName();
		fullName.setFirstName(payload.getFirstName());
		fullName.setLastName(payload.getLastName());

		user.setFullName(fullName);
		user.setMobile(payload.getMobile());
		user.setEmail(payload.getEmail());
		user.setGender(Gender.valueOf(payload.getGender()));

		return userRepository.save(user);
	}

	@Override
	public User updateUserPassword(User user, String password) {
		user.setPassword(passwordEncoder.encode(password));

		return userRepository.save(user);
	}

}
