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

import java.io.File;

import javax.validation.constraints.NotNull;

import com.zymr.zvisitor.util.Util;


public class EmailDTO {
	@NotNull
	private String subject;
	
	@NotNull
	private String message;
	
	@NotNull
	private String from;
	
	@NotNull
	private String to;
	
	private File attachment;	
	
	private String attachmentName;

	public EmailDTO(String subject, String message, String from, String to, File attachment, String attachmentName) {
		Util.validate(subject, message, from, to, attachment, attachmentName);
		this.subject = subject;
		this.message = message;
		this.from = from;
		this.to = to;
		this.attachment = attachment;
		this.attachmentName = attachmentName;
	}
	
	public EmailDTO(String subject, String message, String from, String to) {
		Util.validate(subject, message, from, to);
		this.subject = subject;
		this.message = message;
		this.from = from;
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public String getMessage() {
		return message;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public File getAttachment() {
		return attachment;
	}

	public String getAttachmentName() {
		return attachmentName;
	}
	 
	@Override
	public String toString() {
		return "EmailDTO [subject=" + subject + ", message=" + message + ", from=" + from + ", to=" + to
				+ ", attachment=" + attachment + ", attachmentName=" + attachmentName + "]";
	}

}
