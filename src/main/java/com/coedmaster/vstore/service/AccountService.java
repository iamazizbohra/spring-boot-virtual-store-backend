package com.coedmaster.vstore.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.dto.UpdatePasswordDto;
import com.coedmaster.vstore.dto.request.AccountRequestDto;
import com.coedmaster.vstore.enums.Gender;
import com.coedmaster.vstore.enums.UserRole;
import com.coedmaster.vstore.enums.UserType;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.exception.PasswordMismatchException;
import com.coedmaster.vstore.exception.UsernameAlreadyTakenException;
import com.coedmaster.vstore.model.IUserDetails;
import com.coedmaster.vstore.model.Role;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.model.embeddable.FullName;
import com.coedmaster.vstore.respository.RoleRepository;
import com.coedmaster.vstore.respository.UserRepository;

@Service
public class AccountService implements IAccountService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public User createAdminAccount(AccountRequestDto payload) {
		Role role = getRoleByName(UserRole.ROLE_ADMIN.name());

		return createAccount(UserType.ADMIN, role, payload);
	}

	@Override
	public User createBuyerAccount(AccountRequestDto payload) {
		Role role = getRoleByName(UserRole.ROLE_BUYER.name());

		return createAccount(UserType.BUYER, role, payload);
	}

	@Override
	public User createSellerAccount(AccountRequestDto payload) {
		Role role = getRoleByName(UserRole.ROLE_SELLER.name());

		return createAccount(UserType.SELLER, role, payload);
	}

	private User createAccount(UserType userType, Role role, AccountRequestDto payload) {
		String mobile = payload.getMobile();

		Optional<User> optionalUser = userRepository.findByMobile(mobile);
		if (!optionalUser.isEmpty()) {
			throw new UsernameAlreadyTakenException("Mobile no is already taken");
		}

		FullName fullName = FullName.builder().firstName(payload.getFirstName()).lastName(payload.getLastName())
				.build();

		User user = User.builder().fullName(fullName).mobile(payload.getMobile()).userType(userType)
				.password(passwordEncoder.encode(payload.getPassword())).email(payload.getEmail())
				.gender(Gender.valueOf(payload.getGender())).roles(Collections.singletonList(role)).enabled(true).build();

		User savedUser = userRepository.save(user);

		return savedUser;
	}

	@Override
	public User updateAccount(AccountRequestDto payload) {
		IUserDetails userDetails = (IUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		Optional<User> userOptional = userRepository.findByMobile(payload.getMobile());
		if (!userOptional.isEmpty()) {
			User user = userOptional.get();

			if (!user.getId().equals(userDetails.getId()))
				throw new UsernameAlreadyTakenException("Mobile no is already taken");
		}

		User user = userRepository.findById(userDetails.getId())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		user.setFullName(FullName.builder().firstName(payload.getFirstName()).lastName(payload.getLastName()).build());
		user.setMobile(payload.getMobile());
		user.setEmail(payload.getEmail());
		user.setGender(Gender.valueOf(payload.getGender()));

		// update authentication token in security context
		userDetails.setFirstName(user.getFullName().getFirstName());
		userDetails.setLastName(user.getFullName().getLastName());
		userDetails.setMobile(user.getMobile());
		userDetails.setEmail(user.getEmail());
		userDetails.setGender(user.getGender());

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return userRepository.save(user);
	}

	@Override
	public User updatePassword(UpdatePasswordDto payload) {
		IUserDetails userDetails = (IUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		User user = userRepository.findByMobile(userDetails.getMobile())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		if (!passwordEncoder.matches(payload.getCurrentPassword(), user.getPassword()))
			throw new PasswordMismatchException("Current password does not match");

		user.setPassword(passwordEncoder.encode(payload.getNewPassword()));

		return userRepository.save(user);
	}

	private Role getRoleByName(String name) {
		Role role = roleRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Role not found"));

		return role;
	}

}
