package com.tweetapp.service;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.domain.TweetRequest;
import com.tweetapp.model.Tweet;
import com.tweetapp.repository.TweetRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TweetService {

	@Autowired
	TweetRepository tweetRepository;

	@Autowired
	ObjectMapper objectMapper;

	public void saveTweet(ConsumerRecord<String, String> consumerRecord)
			throws JsonMappingException, JsonProcessingException {

		TweetRequest tweetRequest = objectMapper.readValue(consumerRecord.value(), TweetRequest.class);

		Tweet tweet = Tweet.buildTweet(tweetRequest);

		Tweet savedTweet = tweetRepository.save(tweet);
		log.info("{} Tweet is saved to database", savedTweet);
	}
	
	public void deleteTweet(String tweetId) {
		Optional<Tweet> optionalTweet = tweetRepository.findById(tweetId);
		if(!optionalTweet.isPresent()) {
			throw new IllegalArgumentException("Invalid Tweet Id");
		}
		log.info("Validation is successfull for the Tweet: {}",optionalTweet.get());
		tweetRepository.delete(optionalTweet.get());
		log.info("successfull deleted the Tweet: {}",optionalTweet.get());
	}

}
