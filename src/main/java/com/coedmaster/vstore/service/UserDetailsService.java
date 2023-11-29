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
import com.coedmaster.vstore.respository.UserRepository;
import com.coedmaster.vstore.service.contract.IUserDetailsService;

@Service
public class UserDetailsService implements IUserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
		User user = userRepository.findByMobile(mobile)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with mobile " + mobile));

		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

		UserDetails userDetails = UserDetails.builder().id(user.getId()).userType(user.getUserType())
				.firstName(user.getFullName().getFirstName()).lastName(user.getFullName().getLastName())
				.mobile(user.getMobile()).password(user.getPassword()).email(user.getEmail()).gender(user.getGender())
				.enabled(user.isEnabled()).authorities(authorities).build();

		return userDetails;
	}

}
