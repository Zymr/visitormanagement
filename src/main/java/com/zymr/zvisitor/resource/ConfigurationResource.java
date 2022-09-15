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

import javax.mail.AuthenticationFailedException;
import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sun.mail.util.MailConnectException;
import com.zymr.zvisitor.converter.EmailConfigConverter;
import com.zymr.zvisitor.dbo.config.Email;
import com.zymr.zvisitor.dto.EmailConfigurationDTO;
import com.zymr.zvisitor.dto.ResponseDTO;
import com.zymr.zvisitor.dto.SlackTokenDTO;
import com.zymr.zvisitor.service.ConfigurationService;
import com.zymr.zvisitor.service.EmailService;
import com.zymr.zvisitor.service.SlackService;
import com.zymr.zvisitor.util.Constants;
import com.zymr.zvisitor.util.enums.ZvisitorResource;

@RestController
public class ConfigurationResource {
	private static final Logger logger = LoggerFactory.getLogger(ConfigurationResource.class);

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private SlackService slackService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private EmailConfigConverter eMailConfigConverter;

	@RequestMapping(value = Constants.SLACK_CONFIG_URL, method = RequestMethod.GET)
	@Operation(summary = "Fetch slack auth token")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Fetch slack auth token",
					content = {@Content(mediaType = "application/json")})
	})
	public ResponseEntity<Map<String, Object>> getSlackToken() {
		ResponseEntity<Map<String, Object>>  result = ResponseEntity.notFound().build();
		try {
			ResponseDTO responseDTO = new ResponseDTO(ZvisitorResource.TOKEN.toLowerCase(), configurationService.getUpdatedToken());
			result = ResponseEntity.ok(responseDTO.getResponse());
		} catch(Exception e) {
			logger.error("Exception while fetching slack token", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value = Constants.SLACK_CONFIG_URL, method = RequestMethod.PUT)
	@Operation(summary = "Update slack auth token")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Update slack auth token",
					content = {@Content(mediaType = "application/json")})
	})
	public ResponseEntity<Map<String, Object>> updateSlackToken(@RequestBody @Valid SlackTokenDTO slackToken) {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.status(HttpStatus.BAD_REQUEST).
				body(new ResponseDTO(Constants.RESPONSE_MESSAGE_KEY, Constants.SLACK_TOKEN_INVALID).getResponse());
		try {
			boolean isValid = slackService.isTokenValid(slackToken.getToken());
			if (isValid) {
				configurationService.updateSlackToken(slackToken.getToken());
				result = ResponseEntity.ok().body(new ResponseDTO(Constants.RESPONSE_MESSAGE_KEY, Constants.SLACK_TOKEN_CONFIGURATION_UPDATED).getResponse());
			}
		} catch(Exception e) {
			logger.error("Exception while updating slack token.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value = Constants.EMAIL_CONFIG_URL, method = RequestMethod.GET)
	@Operation(summary = "Fetch mail configuration")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Fetch mail configuration",
					content = {@Content(mediaType = "application/json")})
	})
	public ResponseEntity<Map<String, Object>> getMailConfiguration() {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.badRequest().build();
		try {
			ResponseDTO responseDTO = new ResponseDTO(ZvisitorResource.EMAIL.toLowerCase(), eMailConfigConverter.convertToDTO(configurationService.getMailConfiguration()));
			result = ResponseEntity.ok(responseDTO.getResponse());
		} catch(Exception e) {
			logger.error("Exception while fetching mail configuration.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value = Constants.EMAIL_CONFIG_URL, method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Update mail configuration")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Update mail configuration",
					content = {@Content(mediaType = "application/json")})
	})
	public ResponseEntity<Map<String, Object>> updateMailConfiguration(@RequestBody @Valid EmailConfigurationDTO mailConfigurationDTO) {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.badRequest().build();
		try {
			Email mailConfiguration = eMailConfigConverter.convert(mailConfigurationDTO);
			emailService.authenticateAndLoadConfiguration(mailConfiguration);
			configurationService.updateMailConfig(mailConfiguration);
			result = ResponseEntity.ok().body(new ResponseDTO(Constants.RESPONSE_MESSAGE_KEY, Constants.EMAIL_CONFIGURATION_CONFIGURATION_UPDATED).getResponse());
		} catch(AuthenticationFailedException | MailConnectException e) {
			result = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(Constants.RESPONSE_MESSAGE_KEY, Constants.EMAIl_CONFIG_INVALID).getResponse());
			logger.error("Exception while updating email configuration.", e);
		} catch(IllegalArgumentException e) {
			result = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(Constants.RESPONSE_MESSAGE_KEY, Constants.EMAIl_CONFIG_INVALID).getResponse());
			logger.error("Exception while updating email configuration.", e);
		} catch(Exception e) {
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			logger.error("Exception while updating email configuration.", e);
		}
		return result;
	}

}
