/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.resource;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zymr.zvisitor.converter.OriginConverter;
import com.zymr.zvisitor.converter.VisitorConverter;
import com.zymr.zvisitor.dbo.Origin;
import com.zymr.zvisitor.dbo.Visitor;
import com.zymr.zvisitor.dto.ResponseDTO;
import com.zymr.zvisitor.dto.VisitorDTO;
import com.zymr.zvisitor.dto.VisitorOriginDTO;
import com.zymr.zvisitor.service.VisitorService;
import com.zymr.zvisitor.util.Constants;
import com.zymr.zvisitor.util.JsonUtils;
import com.zymr.zvisitor.util.enums.ZvisitorResource;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(Constants.VISITOR_URL)
public class VisitorResource extends BaseResource {
	private static final Logger logger = LoggerFactory.getLogger(VisitorResource.class);

	@Autowired
	private VisitorService visitorService;

	@Autowired
	private OriginConverter originConverter;

	@Autowired
	private VisitorConverter visitorConverter;

	@RequestMapping(value = "/origins", method = RequestMethod.GET)
	@ApiOperation(value = "Fetch visitor categories", response = ResponseDTO.class)
	public ResponseEntity<Map<String, Object>> getVisitorOrigin() {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.notFound().build();
		try {
			List<Origin> visitorOrigin = visitorService.getVisitorOriginCategories();
			Collection<VisitorOriginDTO> visitorOriginDTO = originConverter.convertToDTO(visitorOrigin);
			if (CollectionUtils.isNotEmpty(visitorOriginDTO)) {
				ResponseDTO responseDTO = new ResponseDTO(ZvisitorResource.origin.toString(), visitorOriginDTO);
				result = ResponseEntity.ok(responseDTO.getResponse());
			}
		} catch(Exception e) {
			logger.error("Exception while fetching all categories.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value = "/origin", method = RequestMethod.POST)
	@ApiOperation(value = "Add new origin")
	public ResponseEntity<Map<String, Object>> addVisitorOrigin(@RequestBody @Valid VisitorOriginDTO visitorOriginDTO) {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.badRequest().build();
		try {
			Origin visitorOrigin = originConverter.convert(visitorOriginDTO);
			Origin dbVisitorOrigin = visitorService.addVisitorOrigin(visitorOrigin);
			if (Objects.nonNull(dbVisitorOrigin)) {
				result = ResponseEntity.status(HttpStatus.CREATED).build();
			} 
		} catch(Exception e) {
			logger.error("Exception while adding new category.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	} 

	@RequestMapping(method = RequestMethod.POST) 
	public ResponseEntity<Map<String, Object>> add(@RequestPart(value = "Profile", required = false) MultipartFile profile,
			@RequestPart("Signature") MultipartFile signature,
			@RequestParam(value = "Visitor", required = true) String visitor) {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.badRequest().build();
		try {
			VisitorDTO visitorDTO = JsonUtils.fromJson(visitor, VisitorDTO.class);
			Visitor dbVisitor = visitorConverter.convert(visitorDTO);
			if (Objects.nonNull(dbVisitor)) {
				visitorService.add(profile, signature, dbVisitor, visitorDTO.getSlackId(), visitorDTO.getChannelId());
				result = ResponseEntity.status(HttpStatus.CREATED).build();
			}
		}  catch(JsonProcessingException e) {
			logger.error("Json Parsing exception.", e);
		}  catch(Exception e) {
			logger.error("Exception while adding visitor.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}
}
