package com.auth.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class UserData {

	@Id
	private String userid;
	@Field
	private String upassword;
	@Field
	private String uname;
	private String authToken;
	@Field
	private String urole;
	@Field
	private String email;
	@Field
	private String gender;
	@Field
	private String age;

}
