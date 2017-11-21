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

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Simple JavaBean domain object representing a Channel.
 */
@Document(collection = SlackChannel.CHANNEL_DOCUMENT)
public class SlackChannel  implements  Serializable 	{
	private static final long serialVersionUID = 6259042069557987159L;

	public static final String CHANNEL_DOCUMENT = "channels";

	public static class SLACKCHANNEL_FIELDS {
		public static final String ID = "_id";
		public static final String CHANNEL_ID = "cId";
		public static final String NAME = "nm";
		public static final String EMAIL = "em";
		public static final String IMAGE = "img";
	}

	@Id
	@Field(SLACKCHANNEL_FIELDS.ID)
	private String id;
	@Indexed(unique = true)
	@Field(SLACKCHANNEL_FIELDS.CHANNEL_ID)
	private String channelId;
	@Field(SLACKCHANNEL_FIELDS.NAME)
	private String name;
	@Field(SLACKCHANNEL_FIELDS.EMAIL)
	private String email;
	@Field(SLACKCHANNEL_FIELDS.IMAGE)
	private String imageSmall;

	public SlackChannel() {
	}

	public SlackChannel(String channelId, String name, String email) {
		this.channelId = channelId;
		this.name = name;
		this.email = email;
	}

	public SlackChannel(String channelId, String name, String email, String imageSmall) {
		this.channelId = channelId;
		this.name = name;
		this.email = email;
		this.imageSmall = imageSmall;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getImageSmall() {
		return imageSmall;
	}

	public void setImageSmall(String imageSmall) {
		this.imageSmall = imageSmall;
	}

	@Override
	public String toString() {
		return "Slack Channel [id=" + id + ", channelId=" + channelId + ", name=" + name + ", email=" + email
				+ ", imageSmall=" + imageSmall + "]";
	}
}
