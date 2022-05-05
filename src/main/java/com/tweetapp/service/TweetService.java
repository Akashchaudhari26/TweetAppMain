package com.tweetapp.service;

import java.util.List;
import java.util.Optional;

import javax.activity.InvalidActivityException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.domain.TweetReplyRequest;
import com.tweetapp.domain.TweetRequest;
import com.tweetapp.model.Like;
import com.tweetapp.model.Reply;
import com.tweetapp.model.Tweet;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TweetService {

	@Autowired
	TweetRepository tweetRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ObjectMapper objectMapper;

	public void saveTweet(ConsumerRecord<String, String> consumerRecord)
			throws JsonMappingException, JsonProcessingException {

		TweetRequest tweetRequest = objectMapper.readValue(consumerRecord.value(), TweetRequest.class);

		Tweet tweet = Tweet.buildTweet(tweetRequest);

		Tweet savedTweet = tweetRepository.save(tweet);
		log.info("{} Tweet is saved to database", savedTweet);
	}

	public void deleteTweet(String tweetId) throws InvalidActivityException {
		Optional<Tweet> optionalTweet = tweetRepository.findById(tweetId);
		if (!optionalTweet.isPresent()) {
			throw new IllegalArgumentException("Invalid Tweet Id");
		}
		Tweet tweet = optionalTweet.get();
		if(!tweet.getLoginId().equals(tweetId)) {
			throw new InvalidActivityException("you connot perform this action");
		}
		
		log.info("Validation is successfull for the Tweet: {}", optionalTweet.get());
		tweetRepository.delete(tweet);
		log.info("successfull deleted the Tweet: {}", optionalTweet.get());
	}

	public Tweet updateTweet(TweetRequest tweetRequest, String tweetId) throws InvalidActivityException {
		Optional<Tweet> optionalTweet = tweetRepository.findById(tweetId);
		if (!optionalTweet.isPresent()) {
			throw new IllegalArgumentException("Invalid Tweet Id");
		}
		Tweet tweet = optionalTweet.get();
		if(!tweet.getLoginId().equals(tweetId)) {
			throw new InvalidActivityException("you connot perform this action");
		}
		log.info("Validation is successfull for the Tweet: {}", optionalTweet.get());
		tweetRequest.setTweetId(tweetId);
		Tweet updatedTweet = Tweet.buildTweet(tweetRequest);
		tweetRepository.save(updatedTweet);
		log.info("successfull updated the Tweet: {}", optionalTweet.get());
		return updatedTweet;
	}

	public void toggleTweetLike(String tweetId, String loginId) {
		Optional<Tweet> optionalTweet = tweetRepository.findById(tweetId);
		if (!optionalTweet.isPresent()) {
			throw new IllegalArgumentException("Invalid Tweet Id");
		}
		log.info("Validation is successfull for the Tweet: {}", optionalTweet.get());
		Tweet tweet = optionalTweet.get();
		List<Like> likes = tweet.getLikes();
		Like like = Like.builder().userLoginId(loginId).build();
		if (likes.contains(like)) {
			likes.remove(like);
			log.info("{} unliked Tweet: {}", like, optionalTweet.get());
		} else {
			likes.add(like);
			log.info("{} liked Tweet: {}", like, optionalTweet.get());
		}
		tweet.setLikes(likes);
		tweetRepository.save(tweet);
	}

	public Tweet replyTweet(TweetReplyRequest replyRequest) {
		Optional<Tweet> optionalTweet = tweetRepository.findById(replyRequest.getTweetId());
		if (!optionalTweet.isPresent()) {
			throw new IllegalArgumentException("Invalid Tweet Id");
		}
		log.info("Validation is successfull for the Tweet: {}", optionalTweet.get());
		Tweet tweet = optionalTweet.get();
		Reply reply = Reply.buildReply(replyRequest);
		List<Reply> replies = tweet.getReplies();
		replies.add(reply);
		tweet.setReplies(replies);
		tweetRepository.save(tweet);
		log.info("{} replied to Tweet: {}", replyRequest.getLoginId(), tweet);
		return tweet;
	}

}
