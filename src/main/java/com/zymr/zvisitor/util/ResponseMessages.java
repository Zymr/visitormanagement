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

public class ResponseMessages {


	public static final String RESPONSE_MESSAGE_KEY = "message";

	//	slack
	public static final String SLACK_TOKEN_INVALID = "Slack token is invalid!";
	public static final String SLACK_TOKEN_NOT_FOUND = "Slack token not found";
	public static final String  SLACK_TOKEN_CONFIGURATION_UPDATED = "Slack token updated successfully";

	//Email		
	public static final String  EMAIL_CONFIGURATION_UPDATED = "Email configuration updated successfully";
	public static final String EMAIl_CONFIG_INVALID = "Email configuration is invalid!";
	public static final String  EMAIL_CONFIGURATION_CONFIGURATION_UPDATED = "Email configuration updated successfully";

	//Location
	public static final String LOCATION_EXISTS = "Duplicate slack channel id or abbreviation provided!";  //400
	public static final String  LOCATION_CONFIGURATION_CONFIGURATION_UPDATED = "Location updated successfully";
	public static final Object LOCATION_ADDED_SUCCESSFLLY = "Location added successfully";
	public static final String  LOCATION_DELETED_SUCCESSFULLY = "Location deleted successfully";
	public static final String  LOCATION_NOT_FOUND = "Location not found"; //404

	//channel
	public static final String  SLACK_DUPLICATE_ID_RESPONSE = "Duplicate slack channel id provided!";
	public static final String  SLACK_INVALID_RESPONSE = "Slack id not found";
	public static final Object SLACK_CHANNEL_ADDED_SUCCESSFLLY = "Slack channel added successfully";
	public static final String  SLACK_CHANNEL_CONFIGURATION_UPDATED = "Slack channel updated successfully";
	public static final String  SLACK_CHANNEL_CONFIGURATION_CONFIGURATION_DELETED = "Slack channel deleted successfully";
}
