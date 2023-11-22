package com.coedmaster.vstore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.model.UserDetailsImpl;
import com.coedmaster.vstore.respository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Transactional
	@Override
	public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
		User user = userRepository.findByMobile(mobile)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with mobile " + mobile));

		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

		UserDetails userDetails = UserDetailsImpl.builder().id(user.getId()).userType(user.getUserType())
				.firstName(user.getFullName().getFirstName()).lastName(user.getFullName().getLastName())
				.mobile(user.getMobile()).password(user.getPassword()).email(user.getEmail()).gender(user.getGender())
				.enabled(user.isEnabled()).authorities(authorities).build();

		return userDetails;
	}

}
