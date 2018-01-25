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

public class OrgSlackConfig {

	private String slackid;		
	private String email;		
	private String abbr;		
	private String name;
	public String getSlackid() {
		return slackid;
	}
	public void setSlackid(String slackid) {
		this.slackid = slackid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAbbr() {
		return abbr;
	}
	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "OrgSlackConfig [slackid=" + slackid + ", email=" + email + ", abbr=" + abbr + ", name=" + name
				+ "]";
	}
}