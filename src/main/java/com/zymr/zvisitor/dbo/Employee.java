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

/** Simple JavaBean domain object representing a Employee. */
@Document(collection = Employee.EMPLOYEE_DOCUMENT)
public class Employee implements  Serializable{
	private static final long serialVersionUID = -2862632799488805428L;

	public static final String EMPLOYEE_DOCUMENT = "employees";

	public static class EMPLOYEE_FIELDS {
		public static final String ID = "_id";
		public static final String SLACK_ID = "sId";
		public static final String NAME = "nm";
		public static final String EMAIL = "em";
		public static final String DELETE = "del";
		public static final String IMAGESMALL = "imgS";
		public static final String IMAGELARGE = "imgl";
		public static final String PHONE = "ph";
		public static final String TEAM_ID = "tmId";
		public static final String SLACK_UPDATEDTIME = "sUt";
		public static final String FULL_NAME = "fNm";
		public static final String TITLE = "tit";
		public static final String LOCATION = "loc";
	}

	@Id
	private String id;	
	@Indexed(unique = true)
	@Field(EMPLOYEE_FIELDS.SLACK_ID)
	private String slackId;
	@Field(EMPLOYEE_FIELDS.NAME)
	private String name;
	@Field(EMPLOYEE_FIELDS.DELETE)
	private boolean deleted;
	@Field(EMPLOYEE_FIELDS.EMAIL)
	private String email;
	@Field(EMPLOYEE_FIELDS.FULL_NAME)
	private String fullName;
	@Field(EMPLOYEE_FIELDS.IMAGESMALL)
	private String imageSmall;
	@Field(EMPLOYEE_FIELDS.IMAGELARGE)
	private String imageLarge;
	@Field(EMPLOYEE_FIELDS.TITLE)
	private String title;
	@Field(EMPLOYEE_FIELDS.PHONE)
	private String phone;
	@Field(EMPLOYEE_FIELDS.TEAM_ID)
	private String teamId;
	@Field(EMPLOYEE_FIELDS.SLACK_UPDATEDTIME)
	private long slackUt;	
	@Field(EMPLOYEE_FIELDS.LOCATION)
	private String location;

	public Employee(String id, String slackId, String name, boolean deleted, String email, String fullName,
			String imageSmall, String imageLarge, String title, String phone, String teamId, long slackUt,
			String location) {
		this.id = id;
		this.slackId = slackId;
		this.name = name;
		this.deleted = deleted;
		this.email = email;
		this.fullName = fullName;
		this.imageSmall = imageSmall;
		this.imageLarge = imageLarge;
		this.title = title;
		this.phone = phone;
		this.teamId = teamId;
		this.slackUt = slackUt;
		this.location = location;
	}

	public Employee() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSlackId() {
		return slackId;
	}

	public void setSlackId(String slackId) {
		this.slackId = slackId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getteamId() {
		return teamId;
	}

	public void setteamId(String teamId) {
		this.teamId = teamId;
	}

	public String getImageSmall() {
		return imageSmall;
	}

	public void setImageSmall(String imageSmall) {
		this.imageSmall = imageSmall;
	}

	public String getImageLarge() {
		return imageLarge;
	}

	public void setImageLarge(String imageLarge) {
		this.imageLarge = imageLarge;
	}

	public long getSlackUt() {
		return slackUt;
	}

	public void setSlackUt(long slackUt) {
		this.slackUt = slackUt;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", slackId=" + slackId + ", name=" + name + ", deleted=" + deleted + ", email="
				+ email + ", fullName=" + fullName + ", imageSmall=" + imageSmall + ", imageLarge=" + imageLarge
				+ ", title=" + title + ", phone=" + phone + ", teamId=" + teamId + ", slackUt=" + slackUt
				+ ", location=" + location + "]";
	}
}
