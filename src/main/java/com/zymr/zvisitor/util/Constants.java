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
	public static final String SLACK_TEMPLATE = "EmployeeNotification.txt";
	public static final String SLACK_CHANNEL_TEMPLATE = "GroupNotification.txt";

	/** Slack and Email notification message placeholders. */
	public static final String EMPLOYEE_NAME = "$employee$";
	public static final String EMPLOYEE_FULLNAME = "$employeename$";
	public static final String VISITOR_FROM = "$from$";
	public static final String VISITOR_PURPOSE = "$purpose$";
	public static final String VISITOR_lOCATION = "$location$";
	public static final String VISITOR_NAME = "$visitor$";
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
	public static final String CHANNEL_URL = Constants.BASE_URL+"/channels";
	public static final String SYNC_CHANNEL = Constants.CHANNEL_URL+"/syncChannels";
	public static final String LOCATION_URL = Constants.BASE_URL+"/location";
	public static final String EMPLOYEE_URL = Constants.BASE_URL+"/employees";
	public static final String GET_EMPLOYEE = Constants.EMPLOYEE_URL+"/{locId}";
	public static final String DELETE_EMPLOYEE = Constants.EMPLOYEE_URL+"/{id}";
	public static final String SYNC_EMPLOYEE = Constants.EMPLOYEE_URL+"/syncEmployee";
	public static final String VISITOR_URL = Constants.BASE_URL+"/visitor";
	public static final String CATEGORIES_URL = Constants.VISITOR_URL+"/origins";
	public static final String CATEGORIES_ADD_URL = Constants.VISITOR_URL+"/origin";
	
	public static final String AUTH_URL = "/auth";
	public static final String CONFIG_URL =  Constants.AUTH_URL+"/config";
	public static final String AUTH_LOCATION_URL =  Constants.CONFIG_URL+"/location";
	public static final String LOCATION_UPDATE_DELETE_URL =  Constants.AUTH_LOCATION_URL+"/{locId}";
	public static final String AUTH_CHANNEL_URL =  Constants.CONFIG_URL+"/channels";
	public static final String CHANNEL_UPDATE_DELETE_URL =  Constants.AUTH_CHANNEL_URL+"/{chId}";
	public static final String SLACK_CONFIG_URL = Constants.CONFIG_URL+"/slack";
	public static final String EMAIL_CONFIG_URL = Constants.CONFIG_URL+"/email";
	public static final String AUTH_VISITOR_URL = Constants.CONFIG_URL+"/visitor";

	public static final String LOGIN_URL = Constants.AUTH_URL +"/login";
	public static final String IMAGE_URL = "/images";
	public static final String FILE_URL = "/files";
	public static final String SWAGGER_URL = "/swagger-ui.html";
	public static final String SWAGGER_VERSION_URL = "/v2/api-docs";
	public static final String SWAGGER_UI = "/configuration/ui";
	public static final String SWAGGER_RESOURCES = "/swagger-resources";
	public static final String SWAGGER_SECURITY = "/configuration/security";
	public static final String SWAGGER_SECURITY_URL = "/webjars/**";

	public static final String RESPONSE_MESSAGE_KEY = "message";
	public static final String FILTER_LOCATION_KEY = "location";
	public static final String FILTER_CATEGORY_KEY = "category";
	public static final String FILTER_FROMDATE_KEY = "from";
	public static final String FILTER_TODATE_KEY = "to";
	
	/** Stored file location. */
	public static final String ZVISITOR_BASEIMAGEDIR = "images";
	public static final String ZVISITOR_IMAGEDIRNAME_FORMAT = "ddMMYYYY";
	public static final String SLACKID_EXISTS = "Slack id alredy exists";

	public static final String IMAGE_EXT = ".png";
	public static final String FORWARD_SLASH = "/";
	public static final String INVALID_PARAM = "Invalid Data Parmeter";
	public static final String NO_DATA_FOUND = "No Data Found";
	
	public static final String SECRET = "qwertylkjhg";
	public static final long EXPIRATION_TIME = 1800000; 
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String UNAUTHORIASED_MESSAGE = "Unauthorized";
	public static final String INVALID_LOGIN = "Invalid username or password!";
	public static final String USER_AUTHENTICATON_ERROR_MSG = "Error while authenticate user token";
	
	public static final String TOKEN = "token";
	public static final String FORWARD_SLASH_AND_ANY = "/**";
	
	/** encryption */
	public static final String ALGORITHM = "PBKDF2WithHmacSHA512";
	public static final String ENCRYPT_TRANSFORMATION = "AES/CBC/PKCS5Padding";
	public static final String ENCRYPT_ALGORITHM_AES = "AES";
	public static final String CHARSET = "UTF8";
	public static final int KEYSIZE = 128;
	
	/** response key */
	public static final String RESPONSE_DATA = "data";
	public static final String RESPONSE_PAGE = "page";
	
	/** request key */
	public static final String REQUEST_PAGESIZE_KEY = "size";
	public static final String REQUEST_PAGENUMBER_KEY = "page";
	
	public static final String DEFAULT_PAGESIZE = "10";
	public static final String DEFAULT_PAGENUMBER = "1";
	
	//Response messages
	public static final String  LOCATION_DELETED_SUCCESSFULLY = "Location deleted successfully";
	public static final String  LOCATION_NOT_FOUND = "Location not found";
	public static final Object LOCATION_ADDED_SUCCESSFLLY = "Location added successfully";
	public static final String  LOCATION_CONFIGURATION_CONFIGURATION_UPDATED = "Location updated successfully";
	public static final String LOCATION_EXISTS = "Duplicate slack channel ID or Abbreviation provided!";

	public static final String  SLACK_TOKEN_CONFIGURATION_UPDATED = "Slack token updated successfully";
	public static final String SLACK_TOKEN_INVALID = "Slack token update failed";

	public static final String  SLACK_CHANNEL_CONFIGURATION_CONFIGURATION_UPDATED = "Slack channel updated successfully";
	public static final String  SLACK_CHANNEL_CONFIGURATION_CONFIGURATION_DELETED = "Slack channel deleted successfully";
	public static final Object SLACK_CHANNEL_ADDED_SUCCESSFLLY = "Slack channel added successfully";
	public static final String  SLACK_DUPLICATE_ID_RESPONSE = "Duplicate slack channel ID provided!";
	
	public static final String  EMAIL_CONFIGURATION_CONFIGURATION_UPDATED = "SMTP configuration updated successfully";
	public static final String EMAIl_CONFIG_INVALID = "SMTP configuration update failed";
	
	public static final String INVALID_CONFIGURATION = "Data is not valid";
}
