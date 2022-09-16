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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;
import javax.mail.internet.AddressException;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zymr.zvisitor.dbo.Employee;
import com.zymr.zvisitor.dbo.Origin;
import com.zymr.zvisitor.dbo.SlackChannel;
import com.zymr.zvisitor.dbo.Visitor;
import com.zymr.zvisitor.dbo.Visitor.VISITOR_FIELDS;
import com.zymr.zvisitor.dto.PageDetails;
import com.zymr.zvisitor.dto.VisitorQueryDTO;
import com.zymr.zvisitor.repository.VisitorOriginRepository;
import com.zymr.zvisitor.repository.VisitorRepository;
import com.zymr.zvisitor.service.config.AppProperties;
import com.zymr.zvisitor.util.Constants;
import com.zymr.zvisitor.util.NdaBuilder;
import com.zymr.zvisitor.util.Util;
import com.zymr.zvisitor.util.enums.ImageType;

@Slf4j
@Service
public class VisitorService {

	@Autowired
	private VisitorOriginRepository visitorOriginRepository; 

	@Autowired
	private VisitorRepository visitorRepository;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private AppProperties appProperties;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private PageService pageService;

	private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ZVISITOR_IMAGEDIRNAME_FORMAT);

	@PostConstruct
	public void init() {
		log.info("Service {} ", toString());
	}

	/** To add visitor categories from property file. */
	public void syncVisitorOrigin() {
		log.info("Syncing of visitor category is started.");
		if (visitorOriginRepository.count() == 0) {
			List<Origin> listOrigin = new ArrayList<>();
			int index = 0;
			Map<String, String> visitorOrigins = appProperties.getVisitorCategory();
			for (Map.Entry<String, String> value : visitorOrigins.entrySet()) {
				listOrigin.add(new Origin(index, value.getKey(), imageService.getImageUrl(ImageType.CATEGORIES, value.getValue())+Constants.IMAGE_EXT));
				index++;
			}
			visitorOriginRepository.saveAll(listOrigin);
			log.info("Syncing of visitor category is done.");
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
		log.info("Started adding visitor with image.");
		try {
			Employee employee = employeeService.getBySlackId(slackId);
			if (Objects.nonNull(employee)) {
				visitor.setEmployee(employee);
			}
			SlackChannel channel = channelService.findByChannelId(channelId);
			if (Objects.nonNull(channel)) {
				visitor.setChannel(channel);
			}
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
						return notificationService.notify(employee, channel, 
								dbVisitor, ndaFile.getAbsolutePath());
					} catch (AddressException | IOException e) {
						log.error("Exception while sending notification", e);
					}
					return null;
				}).exceptionally(e -> { log.error("Exception while sending notification", e); return null; });
			}
		} catch(Exception e) {
			log.error("Exception while adding visitor. {}", visitor, e);
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
		log.info("Uploading visitor images.");
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
		log.info("Images uploaded.");
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

	public PageDetails getWithFilter(Optional<List<String>> locations, Optional<List<String>> categories, Optional<Long> from, Optional<Long> to, 
			int page, int size) {
		Map<String, List<String>> queryParam = new HashMap<String, List<String>>();
		if (locations.isPresent()) {
			queryParam.put(VISITOR_FIELDS.LOCATION, locations.get());
		} 
		if (categories.isPresent()) {
			queryParam.put(VISITOR_FIELDS.CATEGORY_NAME, categories.get());
		}
		VisitorQueryDTO visitorQueryDTO = new VisitorQueryDTO(queryParam);
		if (from.isPresent()) {
			visitorQueryDTO.setFindByGte(new Date(from.get()));
		}
		if (to.isPresent()) {
			visitorQueryDTO.setFindByLte(new Date(to.get()));
		}
		PageRequest pageRequest = PageRequest.of(page - 1, size, Direction.DESC, VISITOR_FIELDS.CREATED_TIME);
		Page<Visitor> ticketPage = visitorRepository.get(visitorQueryDTO, pageRequest);
		return pageService.fillPageDetails(ticketPage);
	}

	public List<Origin> getVisitorOriginCategories() {
		return visitorOriginRepository.findAll();		
	}

	public List<Visitor> getVisitors() {
		return visitorRepository.findAll();
	}

	public List<Visitor> findAllWithEmployeeId() {
		return visitorRepository.findAll(Sort.by(Sort.Direction.ASC, VISITOR_FIELDS.ID));
	}

	@Override
	public String toString() {
		return "VisitorService [visitorOriginRepository=" + visitorOriginRepository + ", visitorRepository="
				+ visitorRepository + ", notificationService=" + notificationService + ", imageService=" + imageService
				+ "]";
	}
}
