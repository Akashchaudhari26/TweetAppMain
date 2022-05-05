package com.tweetapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tweetapp.domain.UserRegisterRequest;

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
	private String loginId;
	@JsonIgnore
	private String password;
	private String phoneNumber;
	
	public static User buildUser(UserRegisterRequest userRegisterRequest) {
		return User.builder()
				.firstName(userRegisterRequest.getFirstName())
				.lastName(userRegisterRequest.getLastName())
				.email(userRegisterRequest.getEmail())
				.loginId(userRegisterRequest.getLoginId())
				.password(userRegisterRequest.getPassword())
				.phoneNumber(userRegisterRequest.getPhoneNumber()).build();
	}
}
