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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document
@EqualsAndHashCode(callSuper = false)
public class Tweet extends AudtitingDetails {
	@Id
	private String id;
	private String loginId;
	private String firstName;
	private String lastName;
	private String message;
	@Default
	private List<String> tags = Collections.emptyList();
	@Default
	private List<Like> likes = Collections.emptyList();
	@Default
	private List<Reply> replies = Collections.emptyList();

	public static Tweet buildTweet(TweetRequest tweetRequest,User user) {
		return Tweet.builder()
				.id(tweetRequest.getTweetId())
				.loginId(tweetRequest.getLoginId())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.message(tweetRequest.getMessage())
				.tags(Arrays.asList(tweetRequest.getTags().split("#")).stream()
						.filter(tag -> !tag.isEmpty()).collect(Collectors.toList()))
				.build();
	}
}
