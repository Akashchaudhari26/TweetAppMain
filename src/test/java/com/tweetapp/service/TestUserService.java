package com.tweetapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.service.UserService;

import scala.Array;

@SpringBootTest
public class TestUserService {
	
	@Mock
	UserRepository userRepository;
	
	@InjectMocks
	UserService userService;
	
	@Test
	public void testGetAllUsers() {
		User user1 = User.builder().firstName("fname").lastName("la").email("som@email.com").loginId("Akki").password("Password@123").build();
		User user2 = User.builder().firstName("fname").lastName("la").email("som1@email.com").loginId("Akki1").password("Password@123").build();
		List<User> userList = new ArrayList<User>();
		
		userList.add(user1);
		userList.add(user2);
		when(userRepository.findAll()).thenReturn(userList);
		assertEquals(userService.getAllUsers(), userList);
	}
	@Test
	public void testGetAllUsers_InvalidCase() {
//		User user1 = User.builder().firstName("fname").lastName("la").email("som@email.com").loginId("Akki").password("Password@123").build();
//		User user2 = User.builder().firstName("fname").lastName("la").email("som1@email.com").loginId("Akki1").password("Password@123").build();
		List<User> userList = new ArrayList<User>();
		
//		userList.add(user1);
//		userList.add(user2);
		when(userRepository.findAll()).thenReturn(userList);
		assertEquals(userService.getAllUsers(), userList);
	}
	
	@Test
	public void testGetAllUsersByLoginId() {
		User user1 = User.builder().firstName("fname").lastName("la").email("som@email.com").loginId("Akki").password("Password@123").build();
		User user2 = User.builder().firstName("fname").lastName("la").email("som1@email.com").loginId("Akki1").password("Password@123").build();
		User user3 = User.builder().firstName("fname").lastName("la").email("so31@email.com").loginId("sam").password("Password@123").build();

		List<User> userList = new ArrayList<>();
		userList.add(user1);
		userList.add(user2);
		userList.add(user3);
		when(userRepository.findByLoginIdLike("Akk")).thenReturn(userList);
		assertEquals(userService.getAllUsersByLoginId("Akk"), userList);
		
	}

}
