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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class LocationDTO {
	@JsonProperty("location_id")
	private String id;

	@NotNull
	@JsonProperty("location")
	private String location;

	@NotNull
	@JsonProperty("location_name")
	private String locationName;

	@NotNull
	@JsonProperty("group_name")
	private String groupName;

	public LocationDTO(String id, String location, String locationName, String groupName) {
		this.id = id;
		this.location = location;
		this.locationName = locationName;
		this.groupName = groupName;
	}

	public LocationDTO() {
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public LocationDTO(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "LocationDTO [id=" + id + ", location=" + location + ", locationName=" + locationName + ", groupName="
				+ groupName + "]";
	}
}
