package com.tweetapp.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tweetapp.domain.TweetRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document
public class Tweet {
	@Id
	private String id;
	private String loginId;
	private String message;
	@Default
	private List<String> tags = Collections.EMPTY_LIST;
	@Default
	private List<Like> likes = Collections.EMPTY_LIST;
	@Default
	private List<Reply> replies = Collections.EMPTY_LIST;

	public static Tweet buildTweet(TweetRequest tweetRequest) {
		return Tweet.builder().id(tweetRequest.getTweetId()).loginId(tweetRequest.getLoginId())
				.message(tweetRequest.getMessage()).tags(Arrays.asList(tweetRequest.getTags().split("#")).stream()
						.filter(tag -> !tag.isEmpty()).collect(Collectors.toList()))
				.build();
	}
}
