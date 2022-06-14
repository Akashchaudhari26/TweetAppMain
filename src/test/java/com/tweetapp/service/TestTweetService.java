package com.tweetapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.domain.TweetReplyRequest;
import com.tweetapp.domain.TweetRequest;
import com.tweetapp.exception.InvalidOperationException;
import com.tweetapp.model.Like;
import com.tweetapp.model.Reply;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;

@SpringBootTest
public class TestTweetService {

	@Mock
	TweetRepository tweetRepository;

	@Mock
	UserRepository userRepository;

	@Spy
	ObjectMapper objectMapper;

	@InjectMocks
	TweetService tweetService;

	@Test
	void deleteTweet_InvalidAction() {
		Tweet tweet = Tweet.builder().loginId("someother").build();
		Optional<Tweet> optional = Optional.of(tweet);
		when(tweetRepository.findById(anyString())).thenReturn(optional);

		InvalidOperationException exception = assertThrows(InvalidOperationException.class, () -> {
			tweetService.deleteTweet("sometweet", "some");
		});
		String expectedMsg = "you cannot perform this action";

		assertEquals(expectedMsg, exception.getMessage());
		verify(tweetRepository, times(0)).delete(tweet);
	}

	@Test
	void deleteTweet_InvalidTweetId() {
		Tweet tweet = Tweet.builder().loginId("someother").build();
		Optional<Tweet> optional = Optional.ofNullable(null);
		when(tweetRepository.findById(anyString())).thenReturn(optional);

		InvalidOperationException exception = assertThrows(InvalidOperationException.class, () -> {
			tweetService.deleteTweet("sometweet", "some");
		});
		String expectedMsg = "Invalid Tweet Id";

		assertEquals(expectedMsg, exception.getMessage());
		verify(tweetRepository, times(0)).delete(tweet);

	}

	@Test
	void deleteTweet_ValidCase() throws InvalidOperationException {

		Tweet tweet = Tweet.builder().loginId("some").build();

		Optional<Tweet> optional = Optional.of(tweet);
		when(tweetRepository.findById(anyString())).thenReturn(optional);
		tweetService.deleteTweet("sometweet", "some");
		verify(tweetRepository, times(1)).delete(tweet);
	}

	@Test
	void updateTweet_InvalidTweetId() {

		TweetRequest tweetRequest = TweetRequest.builder().message("Hello1").tags("").build();

		Tweet tweet = Tweet.builder().loginId("some").build();
		Optional<Tweet> optional = Optional.ofNullable(null);
		when(tweetRepository.findById(anyString())).thenReturn(optional);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			tweetService.updateTweet(tweetRequest, "some");
		});
		String expectedMsg = "Invalid Tweet Id";

		assertEquals(expectedMsg, exception.getMessage());
		verify(tweetRepository, times(0)).save(tweet);
	}

	@Test
	void updateTweet_InvalidAction() {

		TweetRequest tweetRequest = TweetRequest.builder().loginId("loginId").message("Hello1").tags("").build();

		Tweet tweet = Tweet.builder().loginId("someOtherLoginId").build();
		Optional<Tweet> optional = Optional.ofNullable(tweet);
		when(tweetRepository.findById(anyString())).thenReturn(optional);

		InvalidOperationException exception = assertThrows(InvalidOperationException.class, () -> {
			tweetService.updateTweet(tweetRequest, "someTweetId");
		});
		String expectedMsg = "you cannot perform this action";

		assertEquals(expectedMsg, exception.getMessage());
		verify(tweetRepository, times(0)).save(tweet);
	}

	@Test
	void updateTweet_ValidCase() throws InvalidOperationException {

		TweetRequest tweetRequest = TweetRequest.builder().loginId("loginId").message("Hello2").tags("").build();

		Tweet tweet = Tweet.builder().loginId("loginId").message("hello1").build();
		Optional<Tweet> optional = Optional.ofNullable(tweet);
		when(tweetRepository.findById(anyString())).thenReturn(optional);

		Tweet updateTweet = tweetService.updateTweet(tweetRequest, "someTweetId");

		Tweet expectedResult = Tweet.builder().message("Hello2").loginId("loginId").build();

		assertEquals(expectedResult, updateTweet);

	}

	@Test
	void toggleTweetLike_InvalidTweetId() {

		Tweet tweet = Tweet.builder().loginId("some").build();
		Optional<Tweet> optional = Optional.ofNullable(null);
		when(tweetRepository.findById(anyString())).thenReturn(optional);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			tweetService.toggleTweetLike("tweetId", "some");
		});
		String expectedMsg = "Invalid Tweet Id";

		assertEquals(expectedMsg, exception.getMessage());
		verify(tweetRepository, times(0)).save(tweet);
	}

	@Test
	void toggleTweetLike_ValidLike() {

		Tweet tweet = Tweet.builder().loginId("some").likes(new ArrayList<Like>()).build();
		Optional<Tweet> optional = Optional.ofNullable(tweet);
		when(tweetRepository.findById(anyString())).thenReturn(optional);

		tweetService.toggleTweetLike("tweetId", "some");

		verify(tweetRepository, times(1)).save(tweet);

	}

	@Test
	void toggleTweetLike_ValidDislike() {

		ArrayList<Like> list = new ArrayList<Like>();
		list.add(Like.builder().userLoginId("some").build());

		Tweet tweet = Tweet.builder().loginId("some").likes(list).build();
		Optional<Tweet> optional = Optional.ofNullable(tweet);
		when(tweetRepository.findById(anyString())).thenReturn(optional);

		tweetService.toggleTweetLike("tweetId", "some");

		verify(tweetRepository, times(1)).save(tweet);

	}

	@Test
	void replyTweet_InvalidTweetId() {

		TweetReplyRequest tweetReplyRequest = TweetReplyRequest.builder().tweetId("tweetId").loginId("someother")
				.message("some reply").build();

		Tweet tweet = Tweet.builder().loginId("some").build();
		Optional<Tweet> optional = Optional.ofNullable(null);
		when(tweetRepository.findById(anyString())).thenReturn(optional);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			tweetService.replyTweet(tweetReplyRequest);
		});
		String expectedMsg = "Invalid Tweet Id";

		assertEquals(expectedMsg, exception.getMessage());
		verify(tweetRepository, times(0)).save(tweet);

	}

	@Test
	void replyTweet_ValidCase() {
		TweetReplyRequest tweetReplyRequest = TweetReplyRequest.builder().tweetId("tweetId").loginId("some")
				.message("some reply").build();

		ArrayList<Reply> reply = new ArrayList<>();
		reply.add(Reply.builder().userLoginId("").message("somemessage").build());

		Tweet tweet = Tweet.builder().loginId("someId").replies(reply).build();
		Optional<Tweet> optional = Optional.ofNullable(tweet);
		
		when(tweetRepository.findById(anyString())).thenReturn(optional);
		when(userRepository.findByLoginId(anyString())).thenReturn(Arrays.asList(User.builder().build()));

		User user = User.builder().build();
		Reply expectedReply = Reply.buildReply(tweetReplyRequest,user);

		Tweet replyTweet = tweetService.replyTweet(tweetReplyRequest);

		verify(tweetRepository, times(1)).save(tweet);

		assertTrue(replyTweet.getReplies().contains(expectedReply));

	}

	@Test
	void getAllTweets_Tweets() {
		Tweet tweet1 = Tweet.builder().id("id1").loginId("loginId1").message("message1").build();
		Tweet tweet2 = Tweet.builder().id("id2").loginId("loginId2").message("message2").build();

		when(tweetRepository.findAll()).thenReturn(Arrays.asList(tweet1, tweet2));

		List<Tweet> tweets = tweetService.getAllTweets();

		assertEquals(2, tweets.size());

	}

	@Test
	void getAllTweets_ZeroTweets() {

		when(tweetRepository.findAll()).thenReturn(Arrays.asList());

		List<Tweet> tweets = tweetService.getAllTweets();

		assertEquals(0, tweets.size());

	}

	@Test
	void getAllTweetsOfUser_InvalidLoginId() {

		when(userRepository.existsByLoginId(anyString())).thenReturn(false);

		assertThrows(InvalidOperationException.class, () -> {
			tweetService.getAllTweetsOfUser("someuser");
		});

	}

	@Test
	void getAllTweetsOfUser_ZeroTweets() throws InvalidOperationException {

		when(userRepository.existsByLoginId(anyString())).thenReturn(true);
		when(tweetRepository.findAll()).thenReturn(Arrays.asList());

		List<Tweet> tweets = tweetService.getAllTweetsOfUser("");

		assertEquals(0, tweets.size());

	}

	@Test
	void getAllTweetsOfUser_Tweets() throws InvalidOperationException {

		Tweet tweet1 = Tweet.builder().id("id1").loginId("loginId").message("message1").build();
		Tweet tweet2 = Tweet.builder().id("id2").loginId("loginId").message("message2").build();

		when(userRepository.existsByLoginId(anyString())).thenReturn(true);
		when(tweetRepository.findByLoginId(anyString())).thenReturn(Arrays.asList(tweet1, tweet2));

		List<Tweet> tweets = tweetService.getAllTweetsOfUser("loginId");

		assertEquals(2, tweets.size());

	}
	
	@Test
	void saveTweet_ValidCase() {
		TweetRequest tweetRequest = TweetRequest.builder().loginId("").message("Hello1").tags("").build();
		
		User user = User.builder().build();
		when(tweetRepository.save(isA(Tweet.class))).thenReturn(Tweet.buildTweet(tweetRequest,user));
		when(userRepository.findByLoginId(anyString())).thenReturn(Arrays.asList(User.builder().firstName("test").build(),User.builder().firstName("test1").build()));

		
		Tweet tweet = tweetService.saveTweet(tweetRequest);
		
		String expectedMsg = "Hello1";
		List<String> expectedTags = Arrays.asList();
		
		assertEquals(expectedMsg, tweet.getMessage());
		assertEquals(expectedTags, tweet.getTags());
	}
	
}
