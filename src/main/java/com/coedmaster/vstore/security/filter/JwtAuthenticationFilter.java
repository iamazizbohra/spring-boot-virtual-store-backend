package com.coedmaster.vstore.security.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.coedmaster.vstore.exception.InvalidAccessTokenException;
import com.coedmaster.vstore.service.contract.IAuthenticationService;
import com.coedmaster.vstore.service.contract.IUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private IAuthenticationService authenticationService;

	@Autowired
	private IUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jws = getJwsFromRequest(request);

		if (StringUtils.hasText(jws)) {
			if (!authenticationService.validateToken(jws)) {
				throw new InvalidAccessTokenException("Invalid access token");
			}

			if (!authenticationService.isTokenExists(jws)) {
				throw new InvalidAccessTokenException("Invalid access token");
			}

			String uuid = authenticationService.getSubjectFromToken(jws);

			UserDetails userDetails = userDetailsService.loadUserByUuid(uuid);

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}

	private String getJwsFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}

		return null;
	}

}
