package com.tweetapp.controller;

import javax.activity.InvalidActivityException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.domain.ForgotPasswordRequest;
import com.tweetapp.domain.UserRegisterRequest;
import com.tweetapp.model.User;
import com.tweetapp.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1.0/tweets")
public class AuthenticationController {

	@Autowired
	AuthenticationService authenticationService;
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest ) throws InvalidActivityException {
		
		User registeredUser = authenticationService.registerNewUser(userRegisterRequest);
		
		return ResponseEntity.ok(registeredUser);
	}
	
	@GetMapping("/login")
	public ResponseEntity<?> loginUser(Authentication authentication){
		if(null == authentication) {
			return new ResponseEntity<>("oh No!!",HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(authentication.getName());
	}
	
	@GetMapping("/{loginId}/forgot")
	public ResponseEntity<?> forgotPassword(Authentication authentication, @PathVariable String loginId, @RequestBody ForgotPasswordRequest forgotPasswordRequest){
		
		forgotPasswordRequest.setLoginId(loginId);
		
		return ResponseEntity.ok(authentication.getName());
	}
	
}
