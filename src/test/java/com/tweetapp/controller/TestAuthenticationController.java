package com.tweetapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.domain.ForgotPasswordRequest;
import com.tweetapp.domain.LoginRequest;
import com.tweetapp.domain.UserRegisterRequest;
import com.tweetapp.exception.InvalidOperationException;
import com.tweetapp.model.User;
import com.tweetapp.security.jwt.JwtUtils;
import com.tweetapp.service.AuthenticationService;

@SpringBootTest
@AutoConfigureMockMvc
public class TestAuthenticationController {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	AuthenticationService authenticationService;

	@MockBean
	JwtUtils jwtUtils;

	@Test
	void testRegisterUser_ValidInput() throws Exception {

		UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().firstName("FNTest").lastName("LN")
				.email("mail@gmail.com").loginId("llee21").password("Pass@test1").phoneNumber("").build();

		when(authenticationService.registerNewUser(isA(UserRegisterRequest.class)))
				.thenReturn(User.buildUser(userRegisterRequest));

		String valueAsString = objectMapper.writeValueAsString(userRegisterRequest);

		MvcResult mvcResult = mockMvc.perform(
				post("/api/v1.0/tweets/register").content(valueAsString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		
		String expectedStr = "{\"id\":null,\"firstName\":\"FNTest\",\"lastName\":\"LN\",\"email\":\"mail@gmail.com\",\"loginId\":\"llee21\",\"phoneNumber\":\"\"}";
		
		assertEquals(expectedStr, contentAsString);

	}

	@Test
	void testRegisterUser_InvalidFirstName() throws Exception {

		UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().firstName("FN").lastName("LN")
				.email("mail@gmail.com").loginId("llee21").password("Pass@test1").phoneNumber("").build();

		when(authenticationService.registerNewUser(isA(UserRegisterRequest.class)))
				.thenThrow(InvalidOperationException.class);

		String valueAsString = objectMapper.writeValueAsString(userRegisterRequest);

		mockMvc.perform(
				post("/api/v1.0/tweets/register").content(valueAsString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());

	}

	@Test
	void testRegisterUser_InvalidLastName() throws Exception {

		UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().firstName("FNTest").lastName("")
				.email("mail@gmail.com").loginId("llee21").password("Pass@test1").phoneNumber("").build();

		when(authenticationService.registerNewUser(isA(UserRegisterRequest.class)))
				.thenThrow(InvalidOperationException.class);
		String valueAsString = objectMapper.writeValueAsString(userRegisterRequest);

		mockMvc.perform(
				post("/api/v1.0/tweets/register").content(valueAsString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());

	}

	@Test
	void testRegisterUser_InvalidEmail() throws Exception {

		UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().firstName("FNTest").lastName("LN")
				.email("mailmaiom").loginId("llee21").password("Pass@test1").phoneNumber("").build();

		when(authenticationService.registerNewUser(isA(UserRegisterRequest.class)))
				.thenThrow(InvalidOperationException.class);
		String valueAsString = objectMapper.writeValueAsString(userRegisterRequest);

		mockMvc.perform(
				post("/api/v1.0/tweets/register").content(valueAsString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());

	}

	@Test
	void testRegisterUser_InvalidPassword() throws Exception {

		UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().firstName("FNTest").lastName("LN")
				.email("mail@gmail.com").loginId("llee21").password("test1").phoneNumber("").build();

		when(authenticationService.registerNewUser(isA(UserRegisterRequest.class)))
				.thenThrow(InvalidOperationException.class);
		String valueAsString = objectMapper.writeValueAsString(userRegisterRequest);

		mockMvc.perform(
				post("/api/v1.0/tweets/register").content(valueAsString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Test
	void testLogin_InvalidLoginId() throws Exception {
		LoginRequest loginRequest = LoginRequest.builder().loginId("juf22").password("Test@Password1").build();

		when(jwtUtils.generateJwtToken(any())).thenReturn("token");
		when(authenticationService.getUserDetails(anyString())).thenThrow(InvalidOperationException.class);

		String valueAsString = objectMapper.writeValueAsString(loginRequest);

		mockMvc.perform(get("/api/v1.0/tweets/login").content(valueAsString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());

	}

	@Test
	void testLogin_InvalidPassword() throws Exception {
		LoginRequest loginRequest = LoginRequest.builder().loginId("test_user").password("Test!Passwd1").build();

		when(jwtUtils.generateJwtToken(any())).thenReturn("token");
		when(authenticationService.getUserDetails(anyString())).thenThrow(InvalidOperationException.class);

		String valueAsString = objectMapper.writeValueAsString(loginRequest);

		mockMvc.perform(get("/api/v1.0/tweets/login").content(valueAsString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());

	}

	@Test
	void testLogin_InvalidPasswordFormat() throws Exception {
		LoginRequest loginRequest = LoginRequest.builder().loginId("test_user").password("TestPasswd").build();

		when(jwtUtils.generateJwtToken(any())).thenReturn("token");
		when(authenticationService.getUserDetails(anyString())).thenThrow(InvalidOperationException.class);

		String valueAsString = objectMapper.writeValueAsString(loginRequest);

		mockMvc.perform(get("/api/v1.0/tweets/login").content(valueAsString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());

	}

	@Test
	void testLogin_InvalidLoginIdAndPassword() throws Exception {
		LoginRequest loginRequest = LoginRequest.builder().loginId("tester").password("T#swd1").build();

		when(jwtUtils.generateJwtToken(any())).thenReturn("token");
		when(authenticationService.getUserDetails(anyString())).thenThrow(InvalidOperationException.class);

		String valueAsString = objectMapper.writeValueAsString(loginRequest);

		mockMvc.perform(get("/api/v1.0/tweets/login").content(valueAsString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());

	}

	@Test
	void testPasswordChange_Valid() throws Exception {
		ForgotPasswordRequest forgotPasswordRequest = ForgotPasswordRequest.builder().loginId("test_user")
				.password("Update@Password2").build();

		String valueAsString = objectMapper.writeValueAsString(forgotPasswordRequest);

		when(authenticationService.changePassword(isA(ForgotPasswordRequest.class))).thenReturn(
				User.builder().firstName("Fname").lastName("ln").email("yo@gma.com").loginId("eed").build());

		mockMvc.perform(
				post("/api/v1.0/tweets/test_user/forgot").content(valueAsString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

	}

	@Test
	void testPasswordChange_InValidPasswordFormat() throws Exception {
		ForgotPasswordRequest forgotPasswordRequest = ForgotPasswordRequest.builder().loginId("test_user")
				.password("Updateassw").build();

		String valueAsString = objectMapper.writeValueAsString(forgotPasswordRequest);

		when(authenticationService.changePassword(isA(ForgotPasswordRequest.class)))
				.thenThrow(InvalidOperationException.class);

		mockMvc.perform(
				post("/api/v1.0/tweets/test_user/forgot").content(valueAsString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

}
