package com.tweetapp.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.isA;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tweetapp.domain.UserRegisterRequest;
import com.tweetapp.exception.InvalidOperationException;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.service.AuthenticationService;

@SpringBootTest
public class TestAuthenticationService {
	
	@Mock
	UserRepository userRepository;
	
	@Autowired
	AuthenticationService authenticationService;
	
	@Test
	public void testRegisterNewUser_InvalidLoginId() {
		// given
		UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().firstName("fname").lastName("la").email("som@email.com").loginId("some_1").password("Pass@word1").build();
		
		// when
		when(userRepository.existsByLoginId(anyString())).thenReturn(true);
		
		// then
		Exception ex = assertThrows(InvalidOperationException.class, ()->{
				authenticationService.registerNewUser(userRegisterRequest);
		});
		String expectedString = "Login Id already exists";
		
		assertEquals(expectedString, ex.getMessage());
	}
	
	@Test
	public void testRegisterNewUser_InvalidEmail() {
		// given
		UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().firstName("fname").lastName("la").email("som@email.com").loginId("some_1").password("Pass@word1").build();
		
		// when
		when(userRepository.existsByLoginId(anyString())).thenReturn(false);
		when(userRepository.existsByEmail(anyString())).thenReturn(false);
		
		// then
		Exception ex = assertThrows(InvalidOperationException.class, ()->{
				authenticationService.registerNewUser(userRegisterRequest);
		});
		String expectedString = "Email already exists";
		
		assertEquals(expectedString, ex.getMessage());
	}
}
