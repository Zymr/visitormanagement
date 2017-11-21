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

public class Email implements  Serializable{
	private static final long serialVersionUID = -7366366403530455024L;

	public static class MAIL_FIELDS {
		public static final String HOST = "host";
		public static final String PORT = "port";
		public static final String USERNAME = "nm";
		public static final String PASSWORD = "pwd";
		public static final String FROM = "frm";
	}
	
	@Field(MAIL_FIELDS.HOST)
	private String host;
	@Field(MAIL_FIELDS.PORT)
	private int port;
	@Field(MAIL_FIELDS.USERNAME)
	private String username;
	@Field(MAIL_FIELDS.PASSWORD)
	private String password;
	@Field(MAIL_FIELDS.FROM)
	private String from;

	public Email(String host, int port, String username, String password, String from) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.from = from;
	}
	public Email() {
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
		return "Mail [host=" + host + ", port=" + port + ", username=" + username
				+ ", password=" + password + ", from=" + from + "]";
	}
}
