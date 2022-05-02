package com.tweetapp.model;

import com.tweetapp.domain.TweetReplyRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Reply {

	private String userLoginId;
	private String message;

	public static Reply buildReply(TweetReplyRequest replyRequest) {
		return Reply.builder()
				.userLoginId(replyRequest.getLoginId())
				.message(replyRequest.getMessage())
				.build();
	}
}
