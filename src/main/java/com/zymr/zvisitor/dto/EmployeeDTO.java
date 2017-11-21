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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeDTO {
	@JsonProperty("id")
	private String id;

	@JsonProperty("slack_id")
	private String slackId;

	@JsonProperty("name")
	private String name;

	@JsonProperty("delete")
	private boolean deleted;

	@JsonProperty("email")
	private String email;

	@JsonProperty("full_name")
	private String fullName;

	@JsonProperty("image_small")
	private String imageSmall;

	@JsonProperty("image_large")
	private String imageLarge;

	@JsonProperty("title")
	private String title;

	@JsonProperty("phone")
	private String phone;

	@JsonProperty("team_id")
	private String teamId;
	
	@JsonProperty("slack_updatetime")
	private long slackUt;
	
	@JsonProperty("location")
	private String location;
	

	public EmployeeDTO() {
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

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
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
		return "EmployeeDTO [id=" + id + ", slackId=" + slackId + ", name=" + name + ", deleted=" + deleted + ", email="
				+ email + ", fullName=" + fullName + ", imageSmall=" + imageSmall + ", imageLarge=" + imageLarge
				+ ", title=" + title + ", phone=" + phone + ", teamId=" + teamId + ", slackUt=" + slackUt
				+ ", location=" + location + "]";
	}
}
