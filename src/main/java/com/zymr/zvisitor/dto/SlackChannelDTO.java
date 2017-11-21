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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class SlackChannelDTO {
	private String id;

	@JsonProperty("channel_id")
	private String channelId;

	@JsonProperty("full_name")
	private String fullName;

	@JsonProperty("email")
	private String email;

	@JsonProperty("image_small")
	private String imageSmall;

	public SlackChannelDTO(String id, String channelId, String fullName, String email, String imageSmall) {
		this.id = id;
		this.channelId = channelId;
		this.fullName = fullName;
		this.email = email;
		this.imageSmall = imageSmall;
	}

	public SlackChannelDTO(String channelId, String fullName, String email, String imageSmall) {
		this.channelId = channelId;
		this.fullName = fullName;
		this.email = email;
		this.imageSmall = imageSmall;
	}
	
	public SlackChannelDTO() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImageSmall() {
		return imageSmall;
	}

	public void setImageSmall(String imageSmall) {
		this.imageSmall = imageSmall;
	}

	@Override
	public String toString() {
		return "ChannelDTO [id=" + id + ", channelId=" + channelId + ", fullName=" + fullName + ", email=" + email
				+ ", imageSmall=" + imageSmall + "]";
	}
}
