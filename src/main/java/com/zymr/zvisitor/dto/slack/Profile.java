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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Profile {
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("real_name")
	private String fullName;
	
	@JsonProperty("image_72")
	private String image72;
	
	@JsonProperty("image_192")
	private String image192;
	
	@JsonProperty("title")
	private String title;
	
	@JsonProperty("phone")
	private String phone;

	public Profile() {
	}

	public String getImage72() {
		return image72;
	}

	public void setImage72(String image72) {
		this.image72 = image72;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImage192() {
		return image192;
	}

	public void setImage192(String image192) {
		this.image192 = image192;
	}

	@Override
	public String toString() {
		return "Profile [email=" + email + ", fullName=" + fullName + ", image72=" + image72 + ", image192="
				+ image192 + ", title=" + title + ", phone=" + phone + "]";
	}
}
