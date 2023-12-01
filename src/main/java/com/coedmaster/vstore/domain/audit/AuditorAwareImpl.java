package com.coedmaster.vstore.domain.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.coedmaster.vstore.domain.contract.IUserDetails;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();

		String principal = "unknown";

		if (authentication instanceof AnonymousAuthenticationToken) {
			principal = (String) authentication.getPrincipal();
		}

		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			IUserDetails userDetails = (IUserDetails) authentication.getPrincipal();

			principal = userDetails.getFirstName() + " " + userDetails.getLastName();
		}

		return Optional.of(principal);
	}
}