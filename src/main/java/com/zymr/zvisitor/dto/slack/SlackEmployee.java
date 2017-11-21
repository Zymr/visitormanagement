/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.dto.slack;

import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SlackEmployee {
	@JsonProperty("id")
	private String slackId;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("deleted")
	private boolean deleted;
	
	@JsonProperty("profile")
	private Profile profile;
	
	@JsonProperty("team_id")
	private String teamId;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("fullName")
	private String fullName;
	
	@JsonProperty("image")
	private String image;
	
	@JsonProperty("title")
	private String title;
	
	@JsonProperty("phone")
	private String phone;
	
	@JsonProperty("updated")
	private int updatedTime;

	public SlackEmployee() {
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setMail(String email) {
		this.email = email;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isCompanyEmployee(List<String> validEmailList) {
		if (isDeleted() || this.profile == null || StringUtils.isEmpty(this.profile.getEmail()) || CollectionUtils.isEmpty(validEmailList)) {
			return false;
		} 
		return validEmailList.stream().anyMatch(s -> this.profile.getEmail().toLowerCase().contains(s));
	}

	public boolean isNewEmployee(List<String> dbEmployeeList) {
		return CollectionUtils.isEmpty(dbEmployeeList) && !dbEmployeeList.contains(this.slackId);
	}
	
	public boolean isUpdated(long uT) {
		return getUpdatedTime() > uT || getUpdatedTime()==0;
	}

	public int getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(int updatedTime) {
		this.updatedTime = updatedTime;
	}
	

	@Override
	public String toString() {
		return "SlackEmployee [slackId=" + slackId + ", name=" + name + ", deleted=" + deleted + ", profile=" + profile
				+ ", teamId=" + teamId + ", email=" + email + ", fullName=" + fullName + ", image=" + image + ", title="
				+ title + ", phone=" + phone + ", updatedTime=" + updatedTime + "]";
	}
}
