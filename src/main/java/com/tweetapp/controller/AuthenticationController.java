package com.tweetapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.domain.ForgotPasswordRequest;
import com.tweetapp.domain.LoginRequest;
import com.tweetapp.domain.LoginResponse;
import com.tweetapp.domain.UserRegisterRequest;
import com.tweetapp.exception.InvalidOperationException;
import com.tweetapp.model.User;
import com.tweetapp.security.jwt.JwtUtils;
import com.tweetapp.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1.0/tweets")
public class AuthenticationController {

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest ) throws InvalidOperationException {
		
		User registeredUser = authenticationService.registerNewUser(userRegisterRequest);
		
		return ResponseEntity.ok(registeredUser);
	}
	
	@GetMapping("/login")
	public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) throws InvalidOperationException{
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		User user = authenticationService.getUserDetails(loginRequest.getLoginId());
		
		return ResponseEntity.ok(LoginResponse.builder().jwtToken(jwt).user(user).build());
	}
	
	@GetMapping("/{loginId}/forgot")
	public ResponseEntity<?> forgotPassword(Authentication authentication, @PathVariable String loginId, @RequestBody ForgotPasswordRequest forgotPasswordRequest) throws InvalidOperationException{
		
		forgotPasswordRequest.setLoginId(loginId);
		
		User user = authenticationService.changePassword(forgotPasswordRequest);
		
		return ResponseEntity.ok(user);
	}
	
}
