package com.coedmaster.vstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.coedmaster.vstore.model.audit.AuditorAwareImpl;

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
public class JpaConfig {
	
	@Bean
	public AuditorAware<String> auditorProvider() {
		return new AuditorAwareImpl();
	}
}
