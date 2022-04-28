package com.tweetapp.domain;

import com.tweetapp.model.Tweet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TweetLike {

	/**
	 * tweet object
	 */
	private Tweet tweet;
	
	/**
	 * whether the user has liked this post
	 */
	private Boolean isLiked=false;
	
	/**
	 * the total number of likes
	 */
	private int likes=0;
	
	/**
	 * @param single argument tweet
	 */
	public TweetLike(Tweet tweet) {
		super();
		this.tweet = tweet;
	}
	
}
