package com.tweetapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	public List<User> getAllUsers() {
		List<User> users = userRepository.findAll();
		if (users.size() == 0) {
			log.info("There are no users to retrieve");
			return users;
		}
		log.info(users.size() + " users successfully retrieved");
		return users;
	}
	public List<User> getAllUsersByLoginId(String loginIdPattern) {
		List<User> users = userRepository.findByLoginIdLike(loginIdPattern);
		if (users.size() == 0) {
			log.info("There are no users to retrieve");
			return users;
		}
		log.info(users.size() + " users successfully retrieved");
		return users;
	}
}
