package com.coedmaster.vstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.coedmaster.vstore.handler.CustomAccessDeniedHandler;
import com.coedmaster.vstore.security.entrypoint.JwtAuthenticationEntryPoint;
import com.coedmaster.vstore.security.filter.AccountStatusFilter;
import com.coedmaster.vstore.security.filter.JwtAuthenticationFilter;
import com.coedmaster.vstore.service.IUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.securityMatcher("/**").authorizeHttpRequests(authorize -> {
			authorize.requestMatchers(HttpMethod.POST, "/admin/account").permitAll();
			authorize.requestMatchers(HttpMethod.POST, "/buyer/account").permitAll();
			authorize.requestMatchers(HttpMethod.POST, "/seller/account").permitAll();
			authorize.requestMatchers(HttpMethod.POST, "/authenticate").permitAll();

			authorize.requestMatchers("/admin/**").hasRole("ADMIN");
			authorize.requestMatchers("/buyer/**").hasRole("BUYER");
			authorize.requestMatchers("/seller/**").hasRole("SELLER");

			authorize.requestMatchers("/employee/**").permitAll();
			authorize.anyRequest().authenticated();
		}).csrf((csrf) -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(exception -> {
					exception.accessDeniedHandler(customAccessDeniedHandler());
					exception.authenticationEntryPoint(jwtAuthenticationEntryPoint());
				}).addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterAfter(new AccountStatusFilter(), AnonymousAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(IUserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);

		return new ProviderManager(authenticationProvider);
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
		return new JwtAuthenticationEntryPoint();
	}

	@Bean
	public CustomAccessDeniedHandler customAccessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

}
