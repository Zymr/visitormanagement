/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/

package com.zymr.zvisitor.service.config;

public class SlackConfig {
	
		private String token;
		private String userName;
		private String message;
		private String channelmessage;
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public String getUserName() {
			return "@" + userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getChannelmessage() {
			return channelmessage;
		}
		public void setChannelmessage(String channelmessage) {
			this.channelmessage = channelmessage;
		}
		@Override
		public String toString() {
			return "SlackConfig [token=" + token + ", userName=" + userName + ", message=" + message
					+ ", channelmessage=" + channelmessage + "]";
		}
	}