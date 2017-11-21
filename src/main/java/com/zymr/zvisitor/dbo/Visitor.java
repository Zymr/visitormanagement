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
import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/** Simple JavaBean domain object representing a Visitor. */
@Document(collection = Visitor.VISITOR_DOCUMENT)
public class Visitor implements Serializable {
	private static final long serialVersionUID = -5656619435435258543L;

	public static final String VISITOR_DOCUMENT = "visitors";

	public static class VISITOR_FIELDS {
		public static final String ID = "_id";
		public static final String NAME = "nm";
		public static final String EMAIL = "em";
		public static final String PROFILEIMAGE = "visitorPic";
		public static final String SIGNATURE = "visitorSignature";
		public static final String MOBILE = "ph";
		public static final String CREATED_TIME = "ct";
		public static final String PURPOSE = "pur";
		public static final String CATEGORY_NAME = "cat";
		public static final String EMPLOYEE_ID = "eId";
		public static final String CHANNEL_ID = "cId";
		public static final String LOCATION = "loc";
	}

	@Id
	private String id;
	@Field(VISITOR_FIELDS.NAME)
	private String name;
	@Field(VISITOR_FIELDS.EMAIL)
	private String email;
	@Field(VISITOR_FIELDS.PROFILEIMAGE)
	private String visitorPic;
	@Field(VISITOR_FIELDS.SIGNATURE)
	private String visitorSignature;
	@Field(VISITOR_FIELDS.MOBILE)
	private String mobile;
	@Field(VISITOR_FIELDS.CREATED_TIME)
	@CreatedDate
	private Date createdTime;
	@Field(VISITOR_FIELDS.PURPOSE)
	private String purpose;
	@Field(VISITOR_FIELDS.CATEGORY_NAME)
	private String categoryName;
	@Field(VISITOR_FIELDS.EMPLOYEE_ID)
	private String employeeId;
	@Field(VISITOR_FIELDS.CHANNEL_ID)
	private String channelId;
	@Field(VISITOR_FIELDS.LOCATION)
	private String location;

	public Visitor(String name) {
		this.name = name;
	}

	public String getVisitorPic() {
		return visitorPic;
	}

	public void setVisitorPic(String visitorPic) {
		this.visitorPic = visitorPic;
	}

	public String getVisitorSignature() {
		return visitorSignature;
	}

	public void setVisitorSignature(String visitorSignature) {
		this.visitorSignature = visitorSignature;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	@Override
	public String toString() {
		return "Visitor [id=" + id + ", name=" + name + ", email=" + email + ", visitorPic=" + visitorPic
				+ ", visitorSignature=" + visitorSignature + ", mobile=" + mobile + ", createdTime=" + createdTime
				+ ", purpose=" + purpose + ", categoryName=" + categoryName + ", employeeId=" + employeeId
				+ ", channelId=" + channelId + ", location=" + location + "]";
	}
}
