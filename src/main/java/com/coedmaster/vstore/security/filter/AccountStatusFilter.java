package com.coedmaster.vstore.security.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.coedmaster.vstore.exception.AccountInactiveException;
import com.coedmaster.vstore.model.IUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AccountStatusFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();

		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			IUserDetails userDetails = (IUserDetails) authentication.getPrincipal();

			if (!userDetails.isEnabled()) {
				throw new AccountInactiveException("Account is inactive");
			}
		}

		filterChain.doFilter(request, response);
	}

}
