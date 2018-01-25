/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.util.enums;

public enum NotificationKey  {
	TOKEN,
	CHANNEL,
	TEXT,
	USERNAME,
	AS_USER,
	ATTACHMENTS,
	CHANNELS,
	INITIAL_COMMENT,
	FILE,
	TITLE;
	
	public String toLowerCase() {
		return name().toLowerCase();
	}
}