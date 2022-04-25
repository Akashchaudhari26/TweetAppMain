package com.tweetapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.domain.TweetRequest;
import com.tweetapp.producer.TweetProducer;

@RestController
@RequestMapping("/api/v1.0/tweets")
public class TweetController {

	@Autowired
	TweetProducer tweetProducer;

	@PostMapping("/{loginId}/add")
	public ResponseEntity<TweetRequest> postNewTweet(@PathVariable String loginId, @RequestBody @Valid TweetRequest tweetRequest)
			throws Exception {

		tweetRequest.setLoginId(loginId);

		TweetRequest tweet = tweetProducer.sendNewTweet(tweetRequest);

		return new ResponseEntity<>(tweet,HttpStatus.CREATED);
	}
}
