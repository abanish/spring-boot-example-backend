package com.uber.driverdataregistry.interfaces.configuration;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class APIKeyAuthenticationManager implements AuthenticationManager {
	private final List<String> validAPIKeys;

	public APIKeyAuthenticationManager(List<String> validAPIKeys) {
		this.validAPIKeys = validAPIKeys;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final String bearerPrefix = "Bearer ";
		final String principal = (String)authentication.getPrincipal();
		if(!principal.startsWith(bearerPrefix)) {
			throw new BadCredentialsException("Invalid API Key");
		}

		final String apiKey = principal.substring(bearerPrefix.length());
		if(!validAPIKeys.contains(apiKey)) {
			throw new BadCredentialsException("Invalid API Key");
		}

		authentication.setAuthenticated(true);
		return authentication;
	}
}
