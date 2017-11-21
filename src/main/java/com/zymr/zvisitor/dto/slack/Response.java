/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.dto.slack;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
	@JsonProperty("ok")
	private boolean ok;
	
	@JsonProperty("group")
	private Channels group;
	
	@JsonProperty("members")
	private List<SlackEmployee> members;
	
	@JsonProperty("channel")
	private Channels channel;
	
	@JsonProperty("error")
	private String error;	

	public Response() {
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public Channels getGroup() {
		return group;
	}

	public void setGroup(Channels group) {
		this.group = group;
	}

	public List<SlackEmployee> getMembers() {
		return members;
	}

	public void setMembers(List<SlackEmployee> members) {
		this.members = members;
	}

	public Channels getChannel() {
		return channel;
	}

	public void setChannel(Channels channel) {
		this.channel = channel;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "ResponseMembers [ok=" + ok + ", group=" + group + ", members=" + members + ", channel=" + channel
				+ ", error=" + error + "]";
	}
}
