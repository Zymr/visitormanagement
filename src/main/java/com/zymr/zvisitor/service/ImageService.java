/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.service.config.AppConfig;
import com.zymr.zvisitor.service.config.AppProperties;
import com.zymr.zvisitor.util.Constants;
import com.zymr.zvisitor.util.Util;
import com.zymr.zvisitor.util.enums.ImageType;

@Service
public class ImageService {
	private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private AppProperties appProperties;

	private File defaultImageFile;
	private Path baseDirPath;

	@PostConstruct
	public void init() throws IOException {
		logger.info("Image Service", toString());

		AppConfig configProps = appProperties.getConfig();

		baseDirPath = Util.createFileDirectory(configProps.getFileUploadsPath());
		Resource resource = resourceLoader.getResource("classpath:static/images/" + configProps.getDefaultImagePath());
		defaultImageFile = Util.createDefaultImageFile(resource, configProps.getDefaultImagePath());
		if (!Files.exists(baseDirPath))  {
			Files.createDirectory(baseDirPath).toString();
		}
	}

	public String getImageUrl(ImageType imagetype, String name) {
		String imageUrl = null;
		AppConfig configProps = appProperties.getConfig();
		switch  (imagetype) {
		case DEPARTMENT: {
			imageUrl =  configProps.getDepartmentImagesPath() + Constants.FORWARD_SLASH + name;
			break;
		}
		case CATEGORIES: {
			imageUrl =  configProps.getCategoryImagesPath() + Constants.FORWARD_SLASH + name;
			break;
		}
		}
		return imageUrl;
	}

	public URL buildUrl(String fileName) throws MalformedURLException {
		StringBuilder sb = new StringBuilder();
		sb.append(appProperties.getBaseUrl());
		sb.append(fileName);
		return new URL(sb.toString());
	}

	public String createFileUploadPath(ImageType imagetype, String name) throws IOException {
		String filePath = null;
		AppConfig configProps = appProperties.getConfig();
		switch (imagetype) {
		case DEPARTMENT: {
			filePath =  Util.getImagePath(name, baseDirPath.toString(), configProps.getDepartmentImagesPath()).toString();
			break;
		}
		case CATEGORIES: {
			filePath =  Util.getImagePath(name, baseDirPath.toString(), configProps.getCategoryImagesPath()).toString();
			break;
		}
		}
		return filePath;
	}

	public Path getBaseDirPath() {
		return baseDirPath;
	}

	public String getBaseDirPathAsStr() {
		return String.valueOf(baseDirPath);
	}

	public File getDefaultImageFile() {
		return defaultImageFile;
	}

	@Override
	public String toString() {
		return "ImageService [resourceLoader=" + resourceLoader + ", appProperties=" + appProperties
				+ ", defaultImageFile=" + defaultImageFile + ", baseDirPath=" + baseDirPath + "]";
	}
}
