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

import java.util.Map;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zymr.zvisitor.converter.EmailConfigConverter;
import com.zymr.zvisitor.dbo.config.Email;
import com.zymr.zvisitor.dto.EmailConfigurationDTO;
import com.zymr.zvisitor.dto.ResponseDTO;
import com.zymr.zvisitor.service.ConfigurationService;
import com.zymr.zvisitor.service.EmailService;
import com.zymr.zvisitor.service.SlackService;
import com.zymr.zvisitor.util.Constants;
import com.zymr.zvisitor.util.enums.ZvisitorResource;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(Constants.CONFIG_URL)
public class ConfigurationResource extends BaseResource {
	private static final Logger logger = LoggerFactory.getLogger(ConfigurationResource.class);

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private SlackService slackService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private EmailConfigConverter eMailConfigConverter;

	@RequestMapping(value = "/slack", method = RequestMethod.GET)
	@ApiOperation(value = "Fetch slack auth token", response = ResponseDTO.class)
	public ResponseEntity<Map<String, Object>> getSlackToken() {
		ResponseEntity<Map<String, Object>>  result = ResponseEntity.notFound().build();
		try {
			ResponseDTO responseDTO = new ResponseDTO(ZvisitorResource.token.toString(), configurationService.getUpdatedToken());
			result = ResponseEntity.ok(responseDTO.getResponse());
		} catch(Exception e) {
			logger.error("Exception while fetching slack token", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value = "/slack", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiOperation(value = "Update slack auth token")
	public ResponseEntity<Map<String, Object>> updateSlackToken(@RequestParam(value = "token", required = true) @Valid String token) {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.badRequest().build();
		try {
			if (StringUtils.isNotBlank(token)) {
				boolean slackResponse = slackService.isTokenValid(token);
				if (slackResponse) {
					configurationService.updateSlackToken(token);
					result = ResponseEntity.ok().build();
				}
			}
		} catch(Exception e) {
			logger.error("Exception while updating slack token.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value = "/email", method = RequestMethod.GET)
	@ApiOperation(value = "Fetch mail configuration", response = ResponseDTO.class)
	public ResponseEntity<Map<String, Object>> getMailConfiguration() {
		ResponseEntity<Map<String, Object>> result = null;
		try {
			ResponseDTO responseDTO = new ResponseDTO(ZvisitorResource.email.toString(), eMailConfigConverter.convertToDTO(configurationService.getMailConfiguration()));
			result = ResponseEntity.ok(responseDTO.getResponse());
		} catch(Exception e) {
			logger.error("Exception while fetching mail configuration.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value = "/email", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update mail configuration")
	public ResponseEntity<Map<String, Object>> updateMailConfiguration(@RequestBody @Valid EmailConfigurationDTO mailConfigurationDTO) {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.badRequest().build();
		try {
			Email mailConfiguration = eMailConfigConverter.convert(mailConfigurationDTO);
			emailService.authenticateAndLoadConfiguration(mailConfigurationDTO);
			configurationService.updateMailConfig(mailConfiguration);
			result = ResponseEntity.ok().build();
		} catch(MessagingException | ConfigurationException e) {
			logger.error("Exception while updating email configuration.", e);
		} catch(Exception e) {
			logger.error("Exception while updating email configuration.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}
}
