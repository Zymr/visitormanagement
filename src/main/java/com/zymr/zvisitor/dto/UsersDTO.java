/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsersDTO {

	public UsersDTO() {
	}

	public UsersDTO(String email , String password) {
		this.email = email;
		this.password = password;
	}

	@JsonProperty("email")
	private String email;

	@JsonProperty("password")
	private String password;


	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UsersDTO [email=" + email + ", password=" + password + "]";
	}
}
