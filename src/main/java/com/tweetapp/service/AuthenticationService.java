package com.tweetapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tweetapp.domain.ForgotPasswordRequest;
import com.tweetapp.domain.UserRegisterRequest;
import com.tweetapp.exception.InvalidOperationException;
import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;

@Service
public class AuthenticationService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	public User registerNewUser(UserRegisterRequest userRegisterRequest) throws InvalidOperationException {

		List<User> findByLoginId = userRepository.findByLoginId(userRegisterRequest.getLoginId());
		if (findByLoginId.size() > 0) {
			throw new InvalidOperationException("Login Id already exists");
		}
		List<User> findByemail = userRepository.findByEmail(userRegisterRequest.getEmail());
		if (findByemail.size() > 0) {
			throw new InvalidOperationException("Email already exists");
		}
		

		User newUser = User.buildUser(userRegisterRequest);
		
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		
		return userRepository.save(newUser);
	}

	public User getUserDetails(String loginId) throws InvalidOperationException {
		List<User> findByLoginId = userRepository.findByLoginId(loginId);
		if(findByLoginId.size() == 0)
			throw new InvalidOperationException("user not present!!");
		return findByLoginId.get(0);
	}

	public User changePassword(ForgotPasswordRequest forgotPasswordRequest) throws InvalidOperationException {
		List<User> findByLoginId = userRepository.findByLoginId(forgotPasswordRequest.getLoginId());
		if(findByLoginId.size() == 0)
			throw new InvalidOperationException("user not present!!");
		User user = findByLoginId.get(0);
		user.setPassword(passwordEncoder.encode(forgotPasswordRequest.getPassword()));
		
		return userRepository.save(user);
	}

}
