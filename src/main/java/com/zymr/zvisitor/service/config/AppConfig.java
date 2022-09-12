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

import javax.validation.constraints.NotBlank;

public class AppConfig {
	
	@NotBlank
	private String fileUploadsPath;
	private String fileBaseDir;
	private String defaultImagePath = "defaultpic.png";
	private String categoryImagesPath = "/images/categories";
	private String departmentImagesPath = "/images/department";
	
	public String getFileUploadsPath() {
		return fileUploadsPath;
	}
	public void setFileUploadsPath(String fileUploadsPath) {
		this.fileUploadsPath = fileUploadsPath;
	}
	public String getFileBaseDir() {
		return fileBaseDir;
	}
	public void setFileBaseDir(String fileBaseDir) {
		this.fileBaseDir = fileBaseDir;
	}
	public String getDefaultImagePath() {
		return defaultImagePath;
	}
	public void setDefaultImagePath(String defaultImagePath) {
		this.defaultImagePath = defaultImagePath;
	}
	
	public String getCategoryImagesPath() {
		return categoryImagesPath;
	}
	public void setCategoryImagesPath(String categoryImagesPath) {
		this.categoryImagesPath = categoryImagesPath;
	}
	public String getDepartmentImagesPath() {
		return departmentImagesPath;
	}
	public void setDepartmentImagesPath(String departmentImagesPath) {
		this.departmentImagesPath = departmentImagesPath;
	}
	@Override
	public String toString() {
		return "AppConfig [fileUploadsPath=" + fileUploadsPath + ", fileBaseDir=" + fileBaseDir + ", defaultImagePath="
				+ defaultImagePath + ", categoryImagesPath=" + categoryImagesPath + ", departmentImagesPath="
				+ departmentImagesPath + "]";
	}
}