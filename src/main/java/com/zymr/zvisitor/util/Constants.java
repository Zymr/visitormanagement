/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.util;

/** General used constants. */
public class Constants {
	
	/** Slack APIs. */
	public static final String USER_LIST_API = "https://slack.com/api/users.list";
	public static final String GROUP_INFO_API = "https://slack.com/api/groups.info";
	public static final String CHANNEL_INFO_API = "https://slack.com/api/channels.info";
	public static final String SLACK_SEND_NOTIFICATION_API = "https://slack.com/api/chat.postMessage";
	public static final String SLACK_FILE_SEND_API = "https://slack.com/api/files.upload";
	public static final String SLACK_AUTH_TEST = "https://slack.com/api/auth.test";
	public static final String SLACK_AUTH_TOKEN = "?token=";

	/** Slack Response keys. */
	public static final String SLACK_GROUP_PARAMNAME = "group";
	public static final String SLACK_CHANNEL_PARAMNAME = "channel";
	public static final String SLACK_CHANNEL_MEMBERS_PARAMNAME = "members";
	public static final String SLACK_CHANNEL_NOTIFICATION_PARAMNAME = "channel";

	/** Mail body keys. */
	public static final String MAIL_SUBJECT = "subject";
	public static final String MAIL_BODY = "body";
	public static final String MAIL_RECIPIENT  = "recipient";
	public static final String MAIL_ATTACHMENT = "attachment";
	
	/** Smtp configuration param. */
	public static final String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
	public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	public static final String MAIL_SMTP_ENABLE = "mail.smtp.starttls.enable";
	public static final String MAIL_DEBUG = "mail.debug";
	
	/** spring mail configuration. */
	public static final String SPRING_MAIL_HOST = "spring.mail.host";
	public static final String SPRING_MAIL_PORT = "spring.mail.port";
	public static final String SPRING_MAIL_USERNAME = "spring.mail.username";
	public static final String SPRING_MAIL_PASSWORD = "spring.mail.password";
	public static final String SPRING_MAIL_FROM = "mail.from";

	/** Message template name. */
	public static final String EMAIL_TEMPLATE = "EmailTemplate.html";
	public static final String NDA_EMAIL_TEMPLATE = "NDAMail.html";

	/** Slack and Email notification message placeholders. */
	public static final String VISITOR_NAME = "$visitor$";
	public static final String EMPLOYEE_NAME = "$employee$";
	public static final String EMPLOYEE_FULLNAME = "$employeename$";
	public static final String VISITOR_FROM = "$from$";
	public static final String VISITOR_PURPOSE = "$purpose$";
	public static final String VISITOR_lOCATION = "$location$";
	
	public static final String VISITOR_DEFAULT_PURPOSE = "meeting";
	public static final String NOTIFICATION_IMAGE_NAME = "Visitor";
	public static final String NOTIFICATION_ATTACHMENT_NAME = "ZYMR";
	public static final String NOTIFICATION_MAIL_SUBJECT = "Visitor is waiting for you at reception";

	/** NDA configuration. */ 
	public static final String NDA_MAIL_SUBJECT = "Confidentiality Agreement - Non-Disclosure Agreement (NDA) By ZYMR";
	public static final String NDA_ATTACHMENT_NAME = "ZYMR_NDA";
	public static final String NDA_FILENAME = "ZYMR_NDA.pdf";

	/** API url. */
	public static final String BASE_URL = "/zvisitor/1.0";
	public static final String CHANNEL_URL = BASE_URL+"/channels";
	public static final String LOCATION_URL = BASE_URL+"/location";
	public static final String EMPLOYEE_URL = BASE_URL+"/employees";
	public static final String VISITOR_URL = BASE_URL+"/visitor";
	public static final String CONFIG_URL = BASE_URL+"/config";

	/** Stored file location. */
	public static final String ZVISITOR_BASEIMAGEDIR = "images";
	public static final String ZVISITOR_IMAGEDIRNAME_FORMAT = "ddMMYYYY";
	public static final String SLACKID_EXISTS = "Slack id alredy exists";

	public static final String IMAGE_EXT = ".png";
	public static final String HTTP_PROTOCOL = "http";
	public static final String FORWARD_SLASH = "/";
	public static final String INVALID_PARAM = "Invalid Data parmeter";
	public static final String NO_DATA_FOUND = "No Data Found";

}
