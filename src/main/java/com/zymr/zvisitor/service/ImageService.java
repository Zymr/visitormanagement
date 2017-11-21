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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.util.Constants;
import com.zymr.zvisitor.util.Util;
import com.zymr.zvisitor.util.enums.ImageType;

@Service
@ConfigurationProperties(prefix = "config")
public class ImageService {
	private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	private ConfigurationService configurationService;
	
	private File defaultImageFile;
	private Path baseDirPath;
	
	@Value("#{${baseUrl}}")
	private String baseUrl;

	@PostConstruct
	public void init() throws IOException {
		configurationService.setBaseUrl(baseUrl);
		logger.info("Image Service", toString());
		baseDirPath = Util.createFileDirectory(configurationService.getFileUploadsPath(), configurationService.getFileBaseDir());
		Resource resource = resourceLoader.getResource("classpath:static/" + configurationService.getDefaultImagePath());
		defaultImageFile = Util.createDefaultImageFile(resource, configurationService.getDefaultImagePath());
		if (!Files.exists(baseDirPath))  {
			Files.createDirectory(baseDirPath).toString();
		}
	}

	public String getImageUrl(ImageType imagetype, String name) {
		String imageUrl = null;
		switch  (imagetype) {
			case department: {
				imageUrl =  configurationService.getDepartmentImagesPath() + Constants.FORWARD_SLASH + name;
				break;
			}
			case categories: {
				imageUrl =  configurationService.getCategoryImagesPath() + Constants.FORWARD_SLASH + name;
				break;
			}
			default : {
				logger.error("type not found. Type: [{}]", imagetype);
				break;
			}
		}
		return imageUrl;
	}

	public URL buildUrl(String fileName) throws MalformedURLException {
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl);
		sb.append(fileName);
		return new URL(sb.toString());
	}

	public String createFileUploadPath(ImageType imagetype, String name) throws IOException {
		String filePath = null;
		switch (imagetype) {
			case department: {
				filePath =  Util.getImagePath(name, baseDirPath.toString(), configurationService.getDepartmentImagesPath()).toString();
				break;
			}
			case categories: {
				filePath =  Util.getImagePath(name, baseDirPath.toString(), configurationService.getCategoryImagesPath()).toString();
				break;
			}
			default : {
				logger.error("type not found. Type: [{}]", imagetype);
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
		return "ImageService [resourceLoader=" + resourceLoader + ", configurationService=" + configurationService
				+ ", defaultImageFile=" + defaultImageFile + ", baseDirPath=" + baseDirPath + "]";
	}
}