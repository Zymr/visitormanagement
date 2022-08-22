/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.dbo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection =Users.USERS_DOCUMENT)
public class Users implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5388644637021084699L;

	public static final String USERS_DOCUMENT = "users";

	public static class USER_FIELDS {
		public static final String ID = "_id";
		public static final String EMAIL = "email";
		public static final String PASSWORD = "pwd";
	}

	@JsonCreator
	public Users(@JsonProperty("email") String email, @JsonProperty("password") String password) {
		super();
		this.email = email;
		this.password = password;
	}
	
	@Id
	@Field(USER_FIELDS.ID)
	private String id;

	@Indexed(unique = true)
	@Field(USER_FIELDS.EMAIL)
	@NotNull
	@JsonProperty("email")
	private String email;

	@Field(USER_FIELDS.PASSWORD)
	@NotNull
	@JsonProperty("password")
	private String password;

	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
	
	@Override
	public String toString() {
		return "Users [id=" + id + ", name=" + email + ", password=" + password + "]";
	}
}
