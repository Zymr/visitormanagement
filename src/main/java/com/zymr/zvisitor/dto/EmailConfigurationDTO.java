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

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailConfigurationDTO {
	@NotNull
	@JsonProperty("host")
	private String host;
	
	@NotNull
	@JsonProperty("port")
	private int port;
	
	@NotNull
	@JsonProperty("user_name")
	private String userName;
	
	@NotNull
	@JsonProperty("password")
	private String password;
	
	@NotNull
	@JsonProperty("from")
	private String from;

	public EmailConfigurationDTO(String host, int port, String userName, String password, String from) {
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.password = password;
		this.from = from;
	}

	public EmailConfigurationDTO() {
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	@Override
	public String toString() {
		return "SMTPConfiguration [host=" + host + ", port=" + port + ", userName=" + userName + ", password="
				+ password + ", from=" + from + "]";
	}
}
