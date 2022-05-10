package com.tweetapp.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.domain.LoginRequest;
import com.tweetapp.domain.LoginResponse;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class TestUserController {
	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	private String token;

	@MockBean
	UserService userService;

	@BeforeEach
	public void setUp() throws Exception {
		LoginRequest loginRequest = LoginRequest.builder().loginId("test_user").password("Test@Password1").build();
		String json = objectMapper.writeValueAsString(loginRequest);

		MvcResult mvcResult = mockMvc
				.perform(get("/api/v1.0/tweets/login").content(json).contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();

		LoginResponse loginResponse = objectMapper.readValue(contentAsString, LoginResponse.class);

		token = loginResponse.getJwtToken();

	}

	@AfterEach
	public void tearDown() {
		token = "";
	}

	@Test
	public void testGetAllTweets() throws Exception {

		int expectedCount = 1;

		when(userService.getAllUsers())
				.thenReturn(Arrays.asList(User.builder().firstName("test").lastName("e").email("e@test.io").loginId("test_1").build()));

		MvcResult mvcResult = mockMvc
				.perform(get("/api/v1.0/tweets/users/all").header("Authorization", "Bearer " + token)).andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();

		List<Tweet> list = objectMapper.readValue(contentAsString, new TypeReference<List<Tweet>>() {
		});

		assertEquals(expectedCount, list.size());

	}
	
	@Test
	public void testSearchByLoginId() throws Exception {

		int expectedCount = 1;

		when(userService.getAllUsersByLoginId(anyString()))
				.thenReturn(Arrays.asList(User.builder().firstName("test").lastName("e").email("e@test.io").loginId("tedt_1").build()));

		MvcResult mvcResult = mockMvc
				.perform(get("/api/v1.0/tweets/user/search/test").header("Authorization", "Bearer " + token)).andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();

		List<Tweet> list = objectMapper.readValue(contentAsString, new TypeReference<List<Tweet>>() {
		});

		assertEquals(expectedCount, list.size());

	}
	
	@Test
	public void testSearchByLoginId_EmptyList() throws Exception {

		int expectedCount = 0;

		when(userService.getAllUsersByLoginId(anyString()))
				.thenReturn(Arrays.asList());

		MvcResult mvcResult = mockMvc
				.perform(get("/api/v1.0/tweets/user/search/test23ee").header("Authorization", "Bearer " + token)).andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();

		List<Tweet> list = objectMapper.readValue(contentAsString, new TypeReference<List<Tweet>>() {
		});

		assertEquals(expectedCount, list.size());

	}
	
	@Test
	public void testSearchByLoginId_WithNoToken() throws Exception {

		when(userService.getAllUsersByLoginId(anyString()))
				.thenReturn(Arrays.asList(User.builder().firstName("test").lastName("e").email("e@test.io").loginId("tedt_1").build()));
		
		mockMvc.perform(get("/api/v1.0/tweets/user/search/test")).andExpectAll(status().is4xxClientError());

	}
	
	@Test
	public void testGetAllTweets_EmptyList() throws Exception {

		int expectedCount = 0;

		when(userService.getAllUsers())
				.thenReturn(Arrays.asList());

		MvcResult mvcResult = mockMvc
				.perform(get("/api/v1.0/tweets/users/all").header("Authorization", "Bearer " + token)).andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();

		List<Tweet> list = objectMapper.readValue(contentAsString, new TypeReference<List<Tweet>>() {
		});

		assertEquals(expectedCount, list.size());

	}

}
