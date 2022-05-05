package com.tweetapp.config;

import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.tweetapp.filters.JWTTokenGeneratorFilter;
import com.tweetapp.filters.JWTTokenValidatorFilter;
import com.tweetapp.filters.UserOperationValidatorFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.httpBasic();
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().
		cors().disable().csrf().disable()
		.addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
		.addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
		.authorizeRequests()
		.mvcMatchers(HttpMethod.GET, "/api/v1.0/tweets/all").authenticated()
		.mvcMatchers(HttpMethod.GET,"/api/v1.0/tweets/login").authenticated();
//				.addFilterAfter(new UserOperationValidatorFilter(), JWTTokenValidatorFilter.class)
//				.authorizeRequests()
//				.mvcMatchers(HttpMethod.GET,"/api/v1.0/tweets/login").permitAll()
//				.mvcMatchers(HttpMethod.POST,"/api/v1.0/tweets/register").permitAll()
//				.mvcMatchers(HttpMethod.GET,"/api/v1.0/tweets/{loginId:^[A-Za-z0-9_]+$}/forgot").permitAll()
//				.anyRequest().permitAll().and().httpBasic();

		
		
//		.configurationSource(new CorsConfigurationSource() {
//			@Override
//			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//				CorsConfiguration config = new CorsConfiguration();
//				config.setAllowedOrigins(Collections.singletonList("*"));
//				config.setAllowedMethods(Collections.singletonList("*"));
//				config.setAllowCredentials(true);
//				config.setAllowedHeaders(Collections.singletonList("*"));
//				config.setExposedHeaders(Arrays.asList("*"));
//				config.setMaxAge(3600L);
//				return config;
//			}
//		}).and()
//		.csrf().disable()
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
//		return NoOpPasswordEncoder.getInstance();
		return new BCryptPasswordEncoder();
	}
}
