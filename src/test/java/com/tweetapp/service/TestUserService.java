package com.tweetapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;

@SpringBootTest
public class TestUserService {

	@Mock
	UserRepository userRepository;

	@InjectMocks
	UserService userService;

	@Test
	void testGetAllUsers() {
		User user1 = User.builder().firstName("fname").lastName("la").email("som@email.com").loginId("Akki")
				.password("Password@123").build();
		User user2 = User.builder().firstName("fname").lastName("la").email("som1@email.com").loginId("Akki1")
				.password("Password@123").build();
		List<User> userList = Arrays.asList(user1, user2);

		when(userRepository.findAll()).thenReturn(userList);

		List<User> actualResult = userService.getAllUsers();

		assertEquals(userList, actualResult);

	}

	@Test
	void testGetAllUsers_EmptyList() {

		int expectedListSize = 0;

		when(userRepository.findAll()).thenReturn(Arrays.asList());

		List<User> actualResult = userService.getAllUsers();

		assertEquals(expectedListSize, actualResult.size());
	}

	@Test
	void testGetAllUsersByLoginId() {
		User user1 = User.builder().firstName("fname").lastName("la").email("som@email.com").loginId("Akki")
				.password("Password@123").build();
		User user2 = User.builder().firstName("fname").lastName("la").email("som1@email.com").loginId("Akki1")
				.password("Password@123").build();
		List<User> userList = Arrays.asList(user1, user2);

		when(userRepository.findByLoginIdLike("Akk")).thenReturn(userList);

		List<User> actualResult = userService.getAllUsersByLoginId("Akk");

		assertEquals(userList, actualResult);

	}

	@Test
	void testGetAllUsersByLoginId_EmptyList() {

		int expectedListSize = 0;

		List<User> userList = Arrays.asList();

		when(userRepository.findByLoginIdLike("Akk")).thenReturn(userList);

		List<User> actualResult = userService.getAllUsersByLoginId("Akk");

		assertEquals(expectedListSize, actualResult.size());

	}

}
