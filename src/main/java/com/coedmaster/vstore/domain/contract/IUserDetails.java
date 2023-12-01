package com.coedmaster.vstore.domain.contract;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.coedmaster.vstore.enums.Gender;
import com.coedmaster.vstore.enums.UserType;

public interface IUserDetails extends UserDetails {
	Long getId();

	void setId(Long id);

	UserType getUserType();

	void setUserType(UserType userType);

	String getFirstName();

	void setFirstName(String firstName);

	String getLastName();

	void setLastName(String lastName);

	String getMobile();

	void setMobile(String mobile);

	String getEmail();

	void setEmail(String email);

	Gender getGender();

	void setGender(Gender gender);

	@Override
	String getPassword();

	void setPassword(String password);

	boolean getEnabled();

	void setEnabled(boolean enabled);

	void setAuthorities(Collection<? extends GrantedAuthority> authorities);

	@Override
	Collection<? extends GrantedAuthority> getAuthorities();

	@Override
	String getUsername();

	@Override
	boolean isEnabled();

	@Override
	boolean isAccountNonLocked();

	@Override
	boolean isAccountNonExpired();

	@Override
	boolean isCredentialsNonExpired();
}
