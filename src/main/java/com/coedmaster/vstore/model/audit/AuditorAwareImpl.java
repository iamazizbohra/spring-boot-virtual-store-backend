package com.coedmaster.vstore.model.audit;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.coedmaster.vstore.model.contract.IUserDetails;
import com.coedmaster.vstore.service.contract.IAuthenticationService;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Autowired
	private IAuthenticationService authenticationService;

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = authenticationService.getAuthentication();

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