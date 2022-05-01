package com.tweetapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document
public class User {
	@Id
	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String loginId;
	private String phoneNumber;
}
