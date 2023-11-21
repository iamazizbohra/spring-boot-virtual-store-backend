package com.coedmaster.vstore.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.dto.request.RegistrationRequestDto;
import com.coedmaster.vstore.enums.Gender;
import com.coedmaster.vstore.enums.UserRole;
import com.coedmaster.vstore.enums.UserType;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.exception.UsernameAlreadyTakenException;
import com.coedmaster.vstore.model.Role;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.model.embeddable.FullName;
import com.coedmaster.vstore.respository.RoleRepository;
import com.coedmaster.vstore.respository.UserRepository;

@Service
public class RegistrationServiceImpl implements RegistrationService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public User registerAdmin(RegistrationRequestDto payload) {
		Role role = getRoleByName(UserRole.ROLE_ADMIN.name());

		return register(UserType.ADMIN, role, payload);
	}

	@Override
	public User registerBuyer(RegistrationRequestDto payload) {
		Role role = getRoleByName(UserRole.ROLE_BUYER.name());

		return register(UserType.BUYER, role, payload);
	}

	@Override
	public User registerSeller(RegistrationRequestDto payload) {
		Role role = getRoleByName(UserRole.ROLE_SELLER.name());

		return register(UserType.SELLER, role, payload);
	}

	private User register(UserType userType, Role role, RegistrationRequestDto payload) {
		String mobile = payload.getMobile();

		Optional<User> optionalUser = userRepository.findByMobile(mobile);
		if (!optionalUser.isEmpty()) {
			throw new UsernameAlreadyTakenException("Mobile no is already taken");
		}

		FullName fullName = FullName.builder().firstName(payload.getFirstName()).lastName(payload.getLastName())
				.build();

		User user = User.builder().fullName(fullName).mobile(payload.getMobile()).userType(userType)
				.password(passwordEncoder.encode(payload.getPassword())).email(payload.getEmail())
				.gender(payload.getGender().equalsIgnoreCase("male") ? Gender.MALE : Gender.FEMALE)
				.roles(Collections.singleton(role)).enabled(true).build();

		User savedUser = userRepository.save(user);

		return savedUser;
	}

	private Role getRoleByName(String name) {
		Role role = roleRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Role not found"));

		return role;
	}

}
