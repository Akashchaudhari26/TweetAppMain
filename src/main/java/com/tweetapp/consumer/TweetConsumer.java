package com.tweetapp.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tweetapp.service.TweetService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ConditionalOnProperty(value = "spring.kafka.enable", havingValue = "true", matchIfMissing = true)
public class TweetConsumer {

	@Autowired
	TweetService tweetService;
	
	
	@KafkaListener(topics = { "new-tweet" })
	public void onMessage(ConsumerRecord<String, String> consumerRecord) throws JsonMappingException, JsonProcessingException {
		log.info("ConsumerRecord: {}", consumerRecord.toString());
		tweetService.saveTweet(consumerRecord);
	}
}
