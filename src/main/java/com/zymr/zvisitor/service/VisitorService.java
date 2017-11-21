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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;
import javax.mail.internet.AddressException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zymr.zvisitor.dbo.Origin;
import com.zymr.zvisitor.dbo.Visitor;
import com.zymr.zvisitor.dbo.Visitor.VISITOR_FIELDS;
import com.zymr.zvisitor.repository.VisitorOriginRepository;
import com.zymr.zvisitor.repository.VisitorRepository;
import com.zymr.zvisitor.util.Constants;
import com.zymr.zvisitor.util.NdaBuilder;
import com.zymr.zvisitor.util.Util;
import com.zymr.zvisitor.util.enums.ImageType;

@Service
public class VisitorService {
	private static final Logger logger = LoggerFactory.getLogger(VisitorService.class);

	@Autowired
	private VisitorOriginRepository visitorOriginRepository; 

	@Autowired
	private VisitorRepository visitorRepository;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private ImageService imageService;
	
	@Autowired
	private ConfigurationService configurationService;

	private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ZVISITOR_IMAGEDIRNAME_FORMAT);

	@PostConstruct
	public void init() {
		logger.info("Service {} ", toString());
	}

	/** To add visitor categories from property file. */
	public void syncVisitorOrigin() {
		logger.info("Syncing of visitor category is started.");
		if (visitorOriginRepository.count() == 0) {
			List<Origin> listOrigin = new ArrayList<>();
			int index = 0;
			Map<String, String> visitorOrigins = configurationService.getVisitorOrigins();
			for (Map.Entry<String, String> value : visitorOrigins.entrySet()) {
				listOrigin.add(new Origin(index, value.getKey(),
						imageService.getImageUrl(ImageType.categories, value.getValue())+Constants.IMAGE_EXT));
				index++;
			}
			visitorOriginRepository.save(listOrigin);
			logger.info("Syncing of visitor category is done.");
		}
	}


	/** To insert new visitor origin category.
	 * 
	 * @param visitorOrigin
	 * 
	 * @return
	 */
	public Origin addVisitorOrigin(Origin visitorOrigin) {
		return visitorOriginRepository.save(visitorOrigin);
	}


	/** To insert visitor into database and upload visitor signature and image.
	 * 
	 * @param visitorImage
	 * @param visitorSignature
	 * @param visitor
	 * @param slackId
	 * @param channelId
	 */
	public void add(MultipartFile visitorImage, MultipartFile visitorSignature, Visitor visitor, String slackId, String channelId) {
		logger.info("Started adding visitor with image.");
		try {
			Visitor dbVisitor = save(visitor);
			String dateDir = LocalDate.now().format(formatter);
			Path dirPath = Util.createFileDirectory(dateDir, dbVisitor.getId());
			if (Objects.nonNull(visitorImage)) {
				dbVisitor.setVisitorPic(Paths.get(dirPath.toString(), visitorImage.getOriginalFilename()).toString());
			} 
			dbVisitor.setVisitorSignature(Paths.get(dirPath.toString(),	visitorSignature.getOriginalFilename()).toString());

			saveVisitorImages(visitorImage, visitorSignature, dirPath);
			save(dbVisitor);
			File ndaFile = generateNDAFile(dbVisitor);

			if	(Objects.nonNull(dbVisitor) && Objects.nonNull(ndaFile)) {
				CompletableFuture.supplyAsync(()->{
					try {
						return notificationService.notify(slackId, channelId, 
								dbVisitor, ndaFile.getAbsolutePath());
					} catch (AddressException | IOException e) {
						logger.error("Exception while sending notification", e);					
					}
					return null;
				}).exceptionally(e -> { logger.error("Exception while sending notification", e); return null; }); 
			}

		} catch(Exception e) {
			logger.error("Exception while adding visitor. {}", visitor, e);
		}
	}

	/**
	 * To upload visitor images.
	 * 
	 * @param visitorImage multipart file type.
	 * @param visitorSignature multipart file type.
	 * @param path  
	 * 
	 * @throws IOException 
	 */
	private void saveVisitorImages(MultipartFile visitorImage, MultipartFile visitorSignature, Path saveDir) throws IOException {
		logger.info("Uploading visitor images.");
		if (Objects.nonNull(visitorImage)) {
			String profilePicPath = Util.getImagePath(visitorImage.getOriginalFilename(), imageService.getBaseDirPathAsStr(), saveDir.toString()).toString();
			if (StringUtils.isNotBlank(profilePicPath)) {
				visitorImage.transferTo(new File(profilePicPath));
			}
		}
		String signaturePicPath = Util.getImagePath(visitorSignature.getOriginalFilename(), imageService.getBaseDirPathAsStr(), saveDir.toString()).toString();
		if (StringUtils.isNotBlank(signaturePicPath)) {
			visitorSignature.transferTo(new File(signaturePicPath));
		}
	}


	public Visitor save(Visitor visitor) {
		return visitorRepository.save(visitor);
	}

	/**
	 * To generate nda file with visitor signature.
	 * 
	 * @param visitor visitor object  
	 * 
	 * @throws IOException 
	 */
	private File generateNDAFile(Visitor visitor) throws IOException {
		String visitorStoredFilesPath = Util.getImageFullPath(imageService.getBaseDirPathAsStr(),
				visitor.getVisitorSignature());
		String visitorNDAFilePath = new File(visitorStoredFilesPath).getParent();
		Path visitorNDAFileName = Paths.get(visitorNDAFilePath, Constants.NDA_FILENAME);	
		return NdaBuilder.build(visitorNDAFileName, new File(visitorStoredFilesPath), visitor.getName());
	}

	public List<Origin> getVisitorOriginCategories() {
		return visitorOriginRepository.findAll();		
	}

	public List<Visitor> getVisitors() {
		return visitorRepository.findAll();
	}

	public List<Visitor> findAllWithEmployeeId() {
		return visitorRepository.findAll(new Sort(Sort.Direction.ASC, VISITOR_FIELDS.ID));
	}

	@Override
	public String toString() {
		return "VisitorService [visitorOriginRepository=" + visitorOriginRepository + ", visitorRepository="
				+ visitorRepository + ", notificationService=" + notificationService + ", imageService=" + imageService
				+ "]";
	}
}
