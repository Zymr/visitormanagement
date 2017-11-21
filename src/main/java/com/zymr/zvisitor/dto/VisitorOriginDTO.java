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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class VisitorOriginDTO {
	@JsonProperty("id")
	private String id;

	@JsonProperty("index")
	private String index;

	@JsonProperty("category")
	private String category;

	@JsonProperty("image")
	private String imagePath;

	public VisitorOriginDTO(String id, String index, String category, String imagePath) {
		this.id = id;
		this.index = index;
		this.category = category;
		this.imagePath = imagePath;
	}

	public VisitorOriginDTO(String index, String category) {
		this.index = index;
		this.category = category;
	}

	public VisitorOriginDTO() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
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
		return "VisitorOriginDTO [id=" + id + ", index=" + index + ", category=" + category + ", imagePath=" + imagePath
				+ "]";
	}
}
