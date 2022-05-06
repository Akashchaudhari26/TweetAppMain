package com.tweetapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.model.User;
import com.tweetapp.service.UserService;

@RestController
@RequestMapping("/api/v1.0/tweets")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/users/all")
	public ResponseEntity<List<User>> getAllTweets() {
		List<User> tweets = userService.getAllUsers();
		return new ResponseEntity<>(tweets, HttpStatus.OK);
	}
	@GetMapping("/user/search/{loginIdPattern}")
	public ResponseEntity<List<User>> getAllUsersByLoginId(@PathVariable String loginIdPattern) {
		List<User> tweets = userService.getAllUsersByLoginId(loginIdPattern);
		return new ResponseEntity<>(tweets, HttpStatus.OK);
	}
}
