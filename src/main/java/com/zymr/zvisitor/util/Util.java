/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;

/** General utility methods related to ZVisitor application. */
public class Util {

	private Util() {
	}

	/**
	 * @param params
	 */
	public static void validate(Object...params) {
		for (Object param : params) {
			if (Objects.isNull(param)) {
				throw new RuntimeException();
			}
		}
	}
	
	/**
	 * @param baseDirName
	 * @param imageDirName
	 * @return
	 * @throws IOException
	 */
	public static Path createFileDirectory(String baseDirName, String...imageDirName) throws IOException{
		Path path = Paths.get(baseDirName, imageDirName);
		if (Files.notExists(path)) {
			Files.createDirectories(path);
		}	
		return path;
	}

	/**
	 * @param imageName
	 * @param baseDirName
	 * @param imageDirName
	 * @return
	 * @throws IOException
	 */
	public static Path getImagePath(String imageName, String baseDirName, String...imageDirName) throws IOException {
		Path path = Paths.get(baseDirName, imageDirName);
		if (Files.notExists(path)) {
			Files.createDirectories(path);
		}	
		return Paths.get(path.toString(), imageName);	
	}

	/**
	 * @param dirPath
	 * @param dbPath
	 * @return
	 * @throws IOException
	 */
	public static String getImageFullPath(String dirPath, String dbPath) throws IOException {
		return createFileDirectory(dirPath, dbPath).toString();
	}

	/**
	 * This method will return default image when visitor image in not available.
	 * 
	 * @throws IOException 
	 * @return file
	 */
	public static File createDefaultImageFile(Resource resource, String path) throws IOException {
		String fileNameWithOutExt = FilenameUtils.removeExtension(path);
		InputStream inputStream = resource.getInputStream();
		File tempFile = File.createTempFile(fileNameWithOutExt, "."+FilenameUtils.getExtension(path));
		FileUtils.copyInputStreamToFile(inputStream, tempFile);
		return tempFile;
	}

	public static String buildURL(String baseUrl, String name) {
		return baseUrl +  name;
	}

}
