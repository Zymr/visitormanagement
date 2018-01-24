/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/

package com.zymr.zvisitor.service.config;

import java.util.List;

public class Organization {
	
	private List<String> validEmailDomains;
	
	private List<SlackChannelConfig> department;
	private List<SlackChannelConfig> locations;
	
	public List<String> getValidEmailDomains() {
		return validEmailDomains;
	}
	public void setValidEmailDomains(List<String> validEmailDomains) {
		this.validEmailDomains = validEmailDomains;
	}
	public List<SlackChannelConfig> getDepartment() {
		return department;
	}
	public void setDepartment(List<SlackChannelConfig> department) {
		this.department = department;
	}
	public List<SlackChannelConfig> getLocations() {
		return locations;
	}
	public void setLocations(List<SlackChannelConfig> locations) {
		this.locations = locations;
	}
	@Override
	public String toString() {
		return "Organization [validEmailDomains=" + validEmailDomains + ", department=" + department + ", locations="
				+ locations + "]";
	}
}