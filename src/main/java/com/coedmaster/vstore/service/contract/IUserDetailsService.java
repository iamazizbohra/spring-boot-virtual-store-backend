package com.coedmaster.vstore.service.contract;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserDetailsService extends UserDetailsService {

	UserDetails loadUserByUuid(String uuid);

}
