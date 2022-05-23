package com.tweetapp.unit.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.service.TweetService;

@SpringBootTest
public class TestTweetService {

	@Mock
	TweetRepository tweetRepository;

	@Mock
	UserRepository userRepository;

	@Mock
	ObjectMapper objectMapper;
	
	@InjectMocks
	TweetService tweetService;
	
	
}
