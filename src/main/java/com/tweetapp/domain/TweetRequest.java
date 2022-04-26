package com.tweetapp.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TweetRequest {
	private String tweetId;
	private String loginId;
	@NotBlank
	@Size(max = 144)
	private String message;
	@Default
	@Size(max = 50)
	private String tags = "";

}
