package com.tweetapp.producer;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.domain.TweetRequest;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TweetProducer {
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	ObjectMapper objectMapper;

	public TweetRequest sendNewTweet(TweetRequest tweetRequest) throws Exception {

		String tweetId = UUID.randomUUID().toString();
		tweetRequest.setTweetId(tweetId);
		String value = objectMapper.writeValueAsString(tweetRequest);
		
		try {
			 kafkaTemplate.sendDefault(tweetRequest.getTweetId(), value).get();
		} catch (ExecutionException | InterruptedException e) {
			log.error("ExecutionException/InterruptedException; Error Sending the Mesaage and the exception is {}",
					e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Exception; Error Sending the Mesaage and the exception is {}", e.getMessage());
			throw e;
		}

		return tweetRequest;

	}
}
