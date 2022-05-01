package com.tweetapp.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseAudtiting implements AuditorAware<String>  {

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		
		String name = "New User";
		if(null!=authentication) {			
			name = authentication.getName();
		}
		
		return Optional.of(name);
	}

}
