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

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VisitorDTO {
	private String id;

    @NotNull
	@JsonProperty("name")
	private String name;

	@JsonProperty("email")
	private String email;

	@JsonProperty("visitor_image")
	private String visitorPic;

	@JsonProperty("visitor_signature")
	private String visitorSignature;

	@JsonProperty("purpose")
	private String purpose;

	@JsonProperty("mobile")
	private String mobile;

	@NotNull
	@JsonProperty("category_name")
	private String categoryName;

	@JsonProperty("employee_id")
	private String empId;

	@JsonProperty("slack_id")
	private String slackId;

	@JsonProperty("channel_id")
	private String channelId;

	@NotNull
	@JsonProperty("location")
	private String location;
	
	public VisitorDTO(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public VisitorDTO() {
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

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getSlackId() {
		return slackId;
	}

	public void setSlackId(String slackId) {
		this.slackId = slackId;
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
		return "VisitorDTO [id=" + id + ", name=" + name + ", email=" + email + ", visitorPic=" + visitorPic
				+ ", visitorSignature=" + visitorSignature + ", purpose=" + purpose + ", mobile=" + mobile
				+ ", categoryName=" + categoryName + ", empId=" + empId + ", slackId=" + slackId + ", channelId="
				+ channelId + ", location=" + location + "]";
	}
}
