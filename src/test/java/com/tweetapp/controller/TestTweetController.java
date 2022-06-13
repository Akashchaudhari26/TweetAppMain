package com.tweetapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.domain.LoginRequest;
import com.tweetapp.domain.LoginResponse;
import com.tweetapp.domain.TweetReplyRequest;
import com.tweetapp.domain.TweetRequest;
import com.tweetapp.domain.UserRegisterRequest;
import com.tweetapp.exception.InvalidOperationException;
import com.tweetapp.model.Reply;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;
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

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passeordEncoder;

	private String token = "";

	private String testTweetId = "testId";

	private User testUser = null;

	@BeforeEach
	void setUp() throws Exception {

		UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().firstName("test").lastName("testing")
				.email("test@ing.com").loginId("test_user").password(passeordEncoder.encode("Test@Password1")).build();

		testUser = User.buildUser(userRegisterRequest);

		if (userRepository.existsByEmail(testUser.getEmail())) {
			userRepository.findByEmail(testUser.getEmail()).stream().forEach(user -> {
				userRepository.delete(user);
			});
		}

		if (userRepository.existsByLoginId(testUser.getLoginId())) {
			userRepository.findByEmail(testUser.getLoginId()).stream().forEach(user -> {
				userRepository.delete(user);
			});
		}

		testUser = userRepository.save(testUser);

		LoginRequest loginRequest = LoginRequest.builder().loginId("test_user").password("Test@Password1").build();
		String json = objectMapper.writeValueAsString(loginRequest);

		MvcResult mvcResult = mockMvc
				.perform(post("/api/v1.0/tweets/login").content(json).contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();

		LoginResponse loginResponse = objectMapper.readValue(contentAsString, LoginResponse.class);

		token = loginResponse.getJwtToken();

	}

	@AfterEach
	void tearDown() {
		token = "";
		testTweetId = "";
		if (testUser != null)
			userRepository.delete(testUser);
	}

	@Test
	void testUpdateTweet_InvalidCase() throws Exception {

		// given
		TweetRequest tweetRequest = TweetRequest.builder().message("Hello1").tags("").build();
		String json = objectMapper.writeValueAsString(tweetRequest);

		// when
		User user = User.builder().build();
		Tweet updatedTweet = Tweet.buildTweet(tweetRequest,user);

		when(tweetService.updateTweet(isA(TweetRequest.class), anyString())).thenReturn(updatedTweet);

		MvcResult mvcResult = mockMvc.perform(put("/api/v1.0/tweets/test_user2/update/" + testTweetId)
				.header("Authorization", "Bearer " + token).content(json).contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();

		String expectedOutput = "you cannot perform this action!!";
		
		// then
		assertEquals(expectedOutput, contentAsString);

	}
	
	@Test
	void testUpdateTweet_ValidCase() throws Exception {

		// given
		TweetRequest tweetRequest = TweetRequest.builder().message("Hello1").tags("").build();
		String json = objectMapper.writeValueAsString(tweetRequest);

		// when
		User user = User.builder().build();
		Tweet updatedTweet = Tweet.buildTweet(tweetRequest,user);
		
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
	void testPostTweet_InValidMessage() throws Exception {

		TweetRequest tweetRequest = TweetRequest.builder().message("").tags("").build();
		String json = objectMapper.writeValueAsString(tweetRequest);

		when(tweetService.updateTweet(isA(TweetRequest.class), anyString())).thenReturn(null);
		String expectedContent = "message - must not be blank";

		mockMvc.perform(put("/api/v1.0/tweets/test_user/update/" + testTweetId)
				.header("Authorization", "Bearer " + token).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError()).andExpect(content().string(expectedContent));

	}

	@Test
	void testDeleteTweet_ValidCase() throws Exception {

		doNothing().when(tweetService).deleteTweet(anyString(), anyString());

		mockMvc.perform(delete("/api/v1.0/tweets/test_user/delete/" + testTweetId)
				.header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

	}
	
	@Test
	void testDeleteTweet_InvalidCase1() throws Exception {

		doNothing().when(tweetService).deleteTweet(anyString(), anyString());

		mockMvc.perform(delete("/api/v1.0/tweets/test_user2/delete/" + testTweetId)
				.header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());

	}
	
	@Test
	void testPostTweet_InvalidCase() throws Exception {
		
		TweetRequest tweetRequest = TweetRequest.builder().message("test message").tags("").build();
		String json = objectMapper.writeValueAsString(tweetRequest);
		
		
		when(tweetService.saveTweet(isA(TweetRequest.class))).thenReturn(null);
		
		String expectedContent = "you cannot perform this action!!";
		
		mockMvc.perform(post("/api/v1.0/tweets/test_user1/post/")
				.header("Authorization", "Bearer " + token).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError()).andExpect(content().string(expectedContent));
		
	}
	
	@Test
	void testPostTweet_ValidCase() throws Exception {
		
		TweetRequest tweetRequest = TweetRequest.builder().message("test message").tags("").build();
		String json = objectMapper.writeValueAsString(tweetRequest);
		
		User user = User.builder().build();
		
		when(tweetService.saveTweet(isA(TweetRequest.class))).thenReturn(Tweet.buildTweet(tweetRequest,user));
		
		mockMvc.perform(post("/api/v1.0/tweets/test_user/post/")
				.header("Authorization", "Bearer " + token).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());
		
	}

	@Test
	void testDeleteTweet_InValidCase() throws Exception {

		doThrow(new InvalidOperationException("Tweet id not found")).when(tweetService).deleteTweet(anyString(),
				anyString());
		String expectedContent = "Tweet id not found";

		mockMvc.perform(delete("/api/v1.0/tweets/test_user/delete/" + testTweetId)
				.header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError()).andExpect(content().string(expectedContent));

	}

	@Test
	void testLikeTweet() throws Exception {

		doNothing().when(tweetService).toggleTweetLike(testTweetId, "tweetApp");

		mockMvc.perform(put("/api/v1.0/tweets/" + "tweetApp" + "/like/" + testTweetId)
				.header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

	}

	@Test
	void testReplyTweet() throws Exception {

		// given
		TweetRequest tweetRequest = TweetRequest.builder().message("Hello1").tags("").build();
		TweetReplyRequest tweetReplyRequest = TweetReplyRequest.builder().message("Great").loginId("test_user")
				.tweetId(testTweetId).build();
		String json = objectMapper.writeValueAsString(tweetReplyRequest);

		User user = User.builder().build();
		
		Tweet tweet = Tweet.buildTweet(tweetRequest,user);
		Reply reply = Reply.buildReply(tweetReplyRequest,user);
		tweet.setReplies(Arrays.asList(reply));

		// when
		when(tweetService.replyTweet(tweetReplyRequest)).thenReturn(tweet);

		MvcResult mvcResult = mockMvc.perform(post("/api/v1.0/tweets/test_user/reply/" + testTweetId)
				.header("Authorization", "Bearer " + token).content(json).contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();

		Tweet value = objectMapper.readValue(contentAsString, Tweet.class);
		// then

		assertEquals("Hello1", value.getMessage());
	}

	@Test
	void testGetAllTweets() throws Exception {

		int expectedCount = 1;
		TweetRequest tweetRequest = TweetRequest.builder().message("Hello1").tags("").build();

		User user = User.builder().build();
		when(tweetService.getAllTweets()).thenReturn(Arrays.asList(Tweet.buildTweet(tweetRequest,user)));

		MvcResult mvcResult = mockMvc.perform(get("/api/v1.0/tweets/all").header("Authorization", "Bearer " + token))
				.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();

		List<Tweet> list = objectMapper.readValue(contentAsString, new TypeReference<List<Tweet>>() {
		});

		assertEquals(expectedCount, list.size());

	}
	

	@Test
	void testGetAllTweetsOfUser() throws Exception {

		int expectedCount = 1;
		TweetRequest tweetRequest = TweetRequest.builder().message("Hello1").tags("").build();

		User user = User.builder().build();
		
		// when
		when(tweetService.getAllTweetsOfUser(anyString())).thenReturn(Arrays.asList(Tweet.buildTweet(tweetRequest,user)));

		MvcResult mvcResult = mockMvc
				.perform(get("/api/v1.0/tweets/test_user").header("Authorization", "Bearer " + token)).andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();

		List<Tweet> list = objectMapper.readValue(contentAsString, new TypeReference<List<Tweet>>() {
		});

		// assert
		assertEquals(expectedCount, list.size());

	}

}