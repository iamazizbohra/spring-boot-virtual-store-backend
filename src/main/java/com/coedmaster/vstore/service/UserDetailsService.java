package com.coedmaster.vstore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.domain.User;
import com.coedmaster.vstore.domain.UserDetails;
import com.coedmaster.vstore.service.contract.IUserDetailsService;
import com.coedmaster.vstore.service.contract.IUserService;

@Service
public class UserDetailsService implements IUserDetailsService {

	@Autowired
	private IUserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.getUserByMobile(username);

		UserDetails userDetails = getUserDetails(user);

		return userDetails;
	}

	@Override
	public UserDetails loadUserByUuid(String uuid) {
		User user = userService.getUserByUuid(uuid);

		UserDetails userDetails = getUserDetails(user);

		return userDetails;
	}

	private UserDetails getUserDetails(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

		UserDetails userDetails = UserDetails.builder().id(user.getId()).userType(user.getUserType())
				.firstName(user.getFullName().getFirstName()).lastName(user.getFullName().getLastName())
				.mobile(user.getMobile()).password(user.getPassword()).email(user.getEmail()).gender(user.getGender())
				.enabled(user.isEnabled()).authorities(authorities).build();
		
		return userDetails;
	}

}
