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

/** Simple JavaBean domain object representing a Location. */
@Document(collection = Location.LOCATION_DOCUMENT)
public class Location implements  Serializable{
	private static final long serialVersionUID = -4703465935759103119L;
	
	public static final String LOCATION_DOCUMENT = "locations";

	public static class LOCATION_FIELDS {
		public static final String ID = "_id";
		public static final String LOCATION = "loc";
		public static final String LOCATION_NAME = "locNm";
		public static final String GROUP_ID = "grp";
	}
	
	@Id
	@Field(LOCATION_FIELDS.ID)
	private String id;
	@Indexed(unique = true)
	@Field(LOCATION_FIELDS.LOCATION)
	private String location;
	@Indexed(unique = true)
	@Field(LOCATION_FIELDS.GROUP_ID)
	private String groupId;	
	@Field(LOCATION_FIELDS.LOCATION_NAME)
	private String locationName;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Location() {
	}

	public Location(String location, String groupId, String locationName) {
		this.location = location;
		this.groupId = groupId;
		this.locationName = locationName;
	}

	@Override
	public String toString() {
		return "Location [id=" + id + ", location=" + location + ", groupId=" + groupId + ", locationName="
				+ locationName + "]";
	}
}