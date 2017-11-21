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

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = PropertyConfiguration.CONFIGURATION_DOCUMENT)
public class PropertyConfiguration  implements  Serializable {
	private static final long serialVersionUID = -4858200589365682589L;
	
	public static final String CONFIGURATION_DOCUMENT = "config";

	public static class CONFIGURATION_FIELDS {
		public static final String ID = "_id";
		public static final String SLACK_CONFIG = "slack";
		public static final String MAIL_CONFIG = "mail";
	}
	
	@Id
	@Field(CONFIGURATION_FIELDS.ID)
	private String id;

	@Indexed(unique = true)
	@Field(CONFIGURATION_FIELDS.SLACK_CONFIG)
	private Slack slackConfig;
	
	@Indexed(unique = true)
	@Field(CONFIGURATION_FIELDS.MAIL_CONFIG)
	private Email mailConfig;
	
	public PropertyConfiguration(Slack slackConfig, Email mailConfig) {
		this.slackConfig = slackConfig;
		this.mailConfig = mailConfig;
	}

	public PropertyConfiguration() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Slack getSlackConfig() {
		return slackConfig;
	}

	public void setSlackConfig(Slack slackConfig) {
		this.slackConfig = slackConfig;
	}

	public Email getMailConfig() {
		return mailConfig;
	}

	public void setMailConfig(Email mailConfig) {
		this.mailConfig = mailConfig;
	}

	@Override
	public String toString() {
		return "PropertyConfiguration [id=" + id + ", slackConfig=" + slackConfig + ", mailConfig=" + mailConfig + "]";
	}
}
