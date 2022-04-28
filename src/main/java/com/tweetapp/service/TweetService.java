package com.tweetapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.domain.TweetLike;
import com.tweetapp.domain.TweetReplyRequest;
import com.tweetapp.domain.TweetRequest;
import com.tweetapp.model.Like;
import com.tweetapp.model.Reply;
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
		if (!optionalTweet.isPresent()) {
			throw new IllegalArgumentException("Invalid Tweet Id");
		}
		log.info("Validation is successfull for the Tweet: {}", optionalTweet.get());
		tweetRepository.delete(optionalTweet.get());
		log.info("successfull deleted the Tweet: {}", optionalTweet.get());
	}

	public Tweet updateTweet(TweetRequest tweetRequest, String tweetId) {
		Optional<Tweet> optionalTweet = tweetRepository.findById(tweetId);
		if (!optionalTweet.isPresent()) {
			throw new IllegalArgumentException("Invalid Tweet Id");
		}
		log.info("Validation is successfull for the Tweet: {}", optionalTweet.get());
		tweetRequest.setTweetId(tweetId);
		Tweet updatedTweet = Tweet.buildTweet(tweetRequest);
		tweetRepository.save(updatedTweet);
		log.info("successfull deleted the Tweet: {}", optionalTweet.get());
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

	public List<TweetLike> getAllTweets(String loginId) {
		List<Tweet> tweetlist = new ArrayList<Tweet>();
		tweetlist = tweetRepository.findAll();
		List<TweetLike> tweets = new ArrayList<TweetLike>();
		if (tweetlist.size() == 0) {
			log.info("There are no tweets to retrieve");
			return tweets;
		}
		tweets = isLiked(tweetlist, loginId);
		log.info(tweets.size() + " tweets and their like status successfully retrieved");
		return tweets;
	}

	public List<TweetLike> getAllTweetsOfUser(String loginId) {
		List<Tweet> tweetlist = new ArrayList<Tweet>();
		tweetlist = tweetRepository.findByUsername(loginId);
		List<TweetLike> tweets = new ArrayList<TweetLike>();
		if (tweetlist.size() == 0) {
			log.info("There are no tweets to retrieve");
			return tweets;
		}
		tweets = isLiked(tweetlist, loginId);
		log.info(tweets.size() + " tweets and their like status successfully retrieved");
		return tweets;
	}

	/**
	 * @param tweetlist
	 * @param loginId
	 * @return a list of Tweets with likes status updated in tweetlike object
	 */
	public List<TweetLike> isLiked(List<Tweet> tweetlist, String loginId) {
		List<TweetLike> tweets = new ArrayList<TweetLike>();
		for (Tweet t : tweetlist) {
			TweetLike tweetItem = new TweetLike(t);
			if (t.getLikes().contains(loginId)) {
				tweetItem.setIsLiked(true);
			}
			tweetItem.setLikes(t.getLikes().size());
			tweets.add(tweetItem);
		}
		return tweets;
	}

}
