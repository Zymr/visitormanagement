/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.dbo.config;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Field;

public class Slack implements  Serializable {
	private static final long serialVersionUID = 3801905544758792206L;

	public static class SLACK_FIELDS {
		public static final String TOKEN = "token";
		public static final String USERNAME = "nm";
		public static final String MESSAGE = "msg";
		public static final String CHANNEL_MESSAGE = "cmsg";
	}

	@Field(SLACK_FIELDS.TOKEN)
	private String token;
	@Field(SLACK_FIELDS.USERNAME)
	private String username;
	@Field(SLACK_FIELDS.MESSAGE)
	private String message;
	@Field(SLACK_FIELDS.CHANNEL_MESSAGE)
	private String channelMessage;

	public Slack(String token, String username, String message, String channelMessage) {
		this.token = token;
		this.username = username;
		this.message = message;
		this.channelMessage = channelMessage;
	}

	public Slack() {
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getChannelMessage() {
		return channelMessage;
	}

	public void setChannelMessage(String channelMessage) {
		this.channelMessage = channelMessage;
	}

	@Override
	public String toString() {
		return "Slack [token=" + token + ", username=" + username + ", message=" + message
				+ ", channelMessage=" + channelMessage + "]";
	}
}
