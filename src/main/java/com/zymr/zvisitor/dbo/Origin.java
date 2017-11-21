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

/** Simple JavaBean domain object representing a Visitor Origin. */
@Document(collection = Origin.ORIGIN_DOCUMENT)
public class Origin implements Serializable {
	private static final long serialVersionUID = 7439787015116303220L;

	public static final String ORIGIN_DOCUMENT = "origins";

	public static class ORIGIN_FIELDS {
		public static final String ID = "_id";
		public static final String CATEGORY = "cat";
		public static final String IMAGEPATH = "imgPath";
		public static final String INDEX = "ind";
	}

	@Id
	@Field(ORIGIN_FIELDS.ID)
	private String id;
	@Field(ORIGIN_FIELDS.INDEX)
	private String index;
	@Indexed(unique = true)
	@Field(ORIGIN_FIELDS.CATEGORY)
	private String category;
	@Field(ORIGIN_FIELDS.IMAGEPATH)
	private String imagePath;

	public Origin() {
	}

	public Origin(String index, String category, String imagePath) {
		this.index = index;
		this.category = category;
		this.imagePath = imagePath;
	}

	public Origin(int index, String category, String imagePath) {
		this.index = String.valueOf(index);
		this.category = category;
		this.imagePath = imagePath;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@Override
	public String toString() {
		return "Origin [id=" + id + ", index=" + index + ", category=" + category + ", imagePath=" + imagePath + "]";
	}
}
