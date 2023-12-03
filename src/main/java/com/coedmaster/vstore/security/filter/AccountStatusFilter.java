package com.coedmaster.vstore.security.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import com.coedmaster.vstore.model.contract.IUserDetails;
import com.coedmaster.vstore.exception.AccountInactiveException;
import com.coedmaster.vstore.service.contract.IAuthenticationService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AccountStatusFilter extends OncePerRequestFilter {

	@Autowired
	private IAuthenticationService authenticationService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Authentication authentication = authenticationService.getAuthentication();

		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			IUserDetails userDetails = authenticationService.getAuthenticatedUserDetails(authentication);

			if (!userDetails.isEnabled()) {
				throw new AccountInactiveException("Account is inactive");
			}
		}

		filterChain.doFilter(request, response);
	}

}
