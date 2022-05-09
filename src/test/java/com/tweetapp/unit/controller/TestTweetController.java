package com.tweetapp.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.domain.LoginRequest;
import com.tweetapp.domain.LoginResponse;
import com.tweetapp.domain.TweetRequest;
import com.tweetapp.model.Tweet;
import com.tweetapp.service.TweetService;

@SpringBootTest
@AutoConfigureMockMvc
public class TestTweetController {
	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	TweetService tweetService;

	private String token = "";

	private String testTweetId = "testId";

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
		testTweetId = "";
	}

	@Test
	public void testUpdateTweet_ValidCase() throws Exception {

		// given
		TweetRequest tweetRequest = TweetRequest.builder().message("Hello1").tags("").build();
		String json = objectMapper.writeValueAsString(tweetRequest);

		// when
		Tweet updatedTweet = Tweet.buildTweet(tweetRequest);

		when(tweetService.updateTweet(isA(TweetRequest.class), anyString())).thenReturn(updatedTweet);

		MvcResult mvcResult = mockMvc.perform(put("/api/v1.0/tweets/test_user/update/" + testTweetId)
				.header("Authorization", "Bearer " + token).content(json).contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();

		Tweet value = objectMapper.readValue(contentAsString, Tweet.class);
		// then

		assertEquals("Hello1", value.getMessage());
		assertEquals(Arrays.asList(), value.getTags());

	}

	@Test
	public void testPostTweet_InValidMessage() throws Exception {

		TweetRequest tweetRequest = TweetRequest.builder().message("").tags("").build();
		String json = objectMapper.writeValueAsString(tweetRequest);

		when(tweetService.updateTweet(isA(TweetRequest.class), anyString())).thenReturn(null);
		String expectedContent = "message - must not be blank";

		mockMvc.perform(put("/api/v1.0/tweets/test_user/update/" + testTweetId)
				.header("Authorization", "Bearer " + token).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError())
				.andExpect(content().string(expectedContent));

	}

}
