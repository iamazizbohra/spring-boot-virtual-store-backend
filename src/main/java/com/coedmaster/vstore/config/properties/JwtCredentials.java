package com.coedmaster.vstore.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "spring.security.jwt")
@Data
public class JwtCredentials {
	
	private String secret;

	private Integer ttl;
	
}
