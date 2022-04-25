package com.tweetapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.domain.TweetRequest;
import com.tweetapp.producer.TweetProducer;
import com.tweetapp.service.TweetService;

@RestController
@RequestMapping("/api/v1.0/tweets")
public class TweetController {

	@Autowired
	TweetProducer tweetProducer;
	
	@Autowired
	TweetService tweetService;

	@PostMapping("/{loginId}/add")
	public ResponseEntity<TweetRequest> postNewTweet(@PathVariable String loginId, @RequestBody @Valid TweetRequest tweetRequest)
			throws Exception {

		tweetRequest.setLoginId(loginId);

		TweetRequest tweet = tweetProducer.sendNewTweet(tweetRequest);

		return new ResponseEntity<>(tweet,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{loginId}/delete/{tweetId}")
	public ResponseEntity<?> deleteTweet(@PathVariable String loginId,@PathVariable String tweetId){
		tweetService.deleteTweet(tweetId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
