package com.tweetapp.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
public class UserRegisterRequest {
	@NotNull
	@NotBlank
	@Size(min = 3, message = "first name should be atleast 3 characters long")
	private String firstName;
	@NotNull
	@NotBlank
	@Size(min = 1, message = "last name should be atleast 1 character long")
	private String lastName;
	@NotNull
	@NotBlank
	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "invalid email")
	private String email;
	@NotNull
	@NotBlank
	@Pattern(regexp = "^[A-Za-z0-9_]+$", message = "invalid loginId, loginId should match ^[A-Za-z0-9_]+$")
	private String loginId;
	@NotNull
	@NotBlank
	@Size(min = 6, message = "password should be atleast 6 characters long")
	@Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$", message = "password should atleast have: - one digit - one lower case alphabet - one upper case alphabet - any one special character - and no blank spaces")
	private String password;
	@Default
	private String phoneNumber = "";
}
