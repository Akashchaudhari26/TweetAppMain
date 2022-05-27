package com.tweetapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tweetapp.domain.ForgotPasswordRequest;
import com.tweetapp.domain.UserRegisterRequest;
import com.tweetapp.exception.InvalidOperationException;
import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;

@SpringBootTest
public class TestAuthenticationService {

	@Mock
	UserRepository userRepository;

	@Mock
	PasswordEncoder passwordEncoder;

	@InjectMocks
	AuthenticationService authenticationService;

	@Test
	void testRegisterNewUser_InvalidLoginId() {
		// given
		UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().firstName("fname").lastName("la")
				.email("som@email.com").loginId("some_1").password("Pass@word1").build();

		// when
		when(userRepository.existsByLoginId(anyString())).thenReturn(true);

		// then
		Exception ex = assertThrows(InvalidOperationException.class, () -> {
			authenticationService.registerNewUser(userRegisterRequest);
		});
		String expectedString = "Login Id already exists";

		assertEquals(expectedString, ex.getMessage());
	}

	@Test
	void testRegisterNewUser_InvalidEmail() {
		// given
		UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().firstName("fname").lastName("la")
				.email("som@email.com").loginId("some_1").password("Pass@word1").build();

		// when
		when(userRepository.existsByLoginId(anyString())).thenReturn(false);
		when(userRepository.existsByEmail(anyString())).thenReturn(true);

		// then
		Exception ex = assertThrows(InvalidOperationException.class, () -> {
			authenticationService.registerNewUser(userRegisterRequest);
		});
		String expectedString = "Email already exists";

		assertEquals(expectedString, ex.getMessage());
	}

	@Test
	void testRegisterNewUser_ValidCase() throws InvalidOperationException {
		// given
		User expectedUser = User.builder().firstName("fname").lastName("la").email("som@email.com").loginId("some_1")
				.password("Pass@word1").phoneNumber("").build();
		UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().firstName("fname").lastName("la")
				.email("som@email.com").loginId("some_1").password("Pass@word1").build();

		// when
		when(userRepository.existsByLoginId(anyString())).thenReturn(false);
		when(userRepository.existsByEmail(anyString())).thenReturn(false);
		when(userRepository.save(isA(User.class))).thenReturn(User.buildUser(userRegisterRequest));
		when(passwordEncoder.encode(anyString())).thenReturn("AnyString");

		// then

		User registerNewUser = authenticationService.registerNewUser(userRegisterRequest);
		assertEquals(expectedUser, registerNewUser);

	}

	@Test
	void testGetUserDetails_UserExists() {
		UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().firstName("fname").lastName("la")
				.email("som@email.com").loginId("some_1").password("Pass@word1").build();
		when(userRepository.findByLoginId(anyString())).thenReturn(Arrays.asList());

		Exception ex = assertThrows(InvalidOperationException.class, () -> {
			authenticationService.getUserDetails(userRegisterRequest.getLoginId());
		});
		String expectedString = "user not present!!";

		assertEquals(expectedString, ex.getMessage());

	}

	@Test
	void testGetUserDetails_UserDetails() throws InvalidOperationException {
		User expectedUser = User.builder().firstName("fname").lastName("la").email("som@email.com").loginId("some_1")
				.password("Pass@word1").phoneNumber("").build();
		UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().firstName("fname").lastName("la")
				.email("som@email.com").loginId("some_1").password("Pass@word1").build();
		when(userRepository.findByLoginId(anyString())).thenReturn(Arrays.asList(User.buildUser(userRegisterRequest)));

		User requiredUser = authenticationService.getUserDetails(userRegisterRequest.getLoginId());
		// then
		assertEquals(expectedUser, requiredUser);

	}

	@Test
	void testChangePassword_UserExists() {
		UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().firstName("fname").lastName("la")
				.email("som@email.com").loginId("some_1").password("Pass@word1").build();
		when(userRepository.findByLoginId(anyString())).thenReturn(Arrays.asList());

		Exception ex = assertThrows(InvalidOperationException.class, () -> {
			authenticationService.getUserDetails(userRegisterRequest.getLoginId());
		});
		String expectedString = "user not present!!";

		assertEquals(expectedString, ex.getMessage());
	}

	@Test
	void testChangePassword_ChangePassword() throws InvalidOperationException {

		// given
		User updatedUser = User.builder().firstName("fname").lastName("la").email("som@email.com").loginId("Akki")
				.password("Password@123").build();

		User user = User.builder().firstName("fname").lastName("la").email("som@email.com").loginId("Akki")
				.password("Password@12").build();
		ForgotPasswordRequest forgotPasswordRequest = ForgotPasswordRequest.builder().loginId("Akki")
				.password("Password@123").build();

		// when
		when(userRepository.findByLoginId(anyString())).thenReturn(Arrays.asList(user));
		when(passwordEncoder.encode(anyString())).thenReturn("Password@123");
		when(userRepository.save(isA(User.class))).thenReturn(updatedUser);

		// then

		User actualUser = authenticationService.changePassword(forgotPasswordRequest);
		String expectedpassword = "Password@123";
		assertEquals(expectedpassword, actualUser.getPassword());

	}
}
