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

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import javax.mail.AuthenticationFailedException;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sun.mail.util.MailConnectException;
import com.zymr.zvisitor.converter.EmailConfigConverter;
import com.zymr.zvisitor.converter.LocationConverter;
import com.zymr.zvisitor.converter.SlackChannelConverter;
import com.zymr.zvisitor.dbo.Location;
import com.zymr.zvisitor.dbo.SlackChannel;
import com.zymr.zvisitor.dbo.config.Email;
import com.zymr.zvisitor.dto.EmailConfigurationDTO;
import com.zymr.zvisitor.dto.LocationDTO;
import com.zymr.zvisitor.dto.ResponseDTO;
import com.zymr.zvisitor.dto.SlackChannelDTO;
import com.zymr.zvisitor.dto.SlackTokenDTO;
import com.zymr.zvisitor.exception.InvalidDataException;
import com.zymr.zvisitor.exception.NoDataFoundException;
import com.zymr.zvisitor.service.ChannelService;
import com.zymr.zvisitor.service.ConfigurationService;
import com.zymr.zvisitor.service.EmailService;
import com.zymr.zvisitor.service.LocationService;
import com.zymr.zvisitor.service.SlackService;
import com.zymr.zvisitor.util.Constants;
import com.zymr.zvisitor.util.JsonUtils;
import com.zymr.zvisitor.util.ResponseMessages;
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

	@Autowired
	private LocationService locationService;

	@Autowired
	private LocationConverter locationConverter;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private SlackChannelConverter slackChannelConverter;

	@RequestMapping(value = "/slack", method = RequestMethod.GET)
	@ApiOperation(value = "Fetch slack auth token", response = ResponseDTO.class)
	public ResponseEntity<Map<String, Object>> getSlackToken() {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.status(HttpStatus.NOT_FOUND).
				body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, ResponseMessages.SLACK_TOKEN_NOT_FOUND).getResponse());
		try {
			ResponseDTO responseDTO = new ResponseDTO(ZvisitorResource.TOKEN.toLowerCase(), configurationService.getUpdatedToken());
			result = ResponseEntity.ok(responseDTO.getResponse());
		} catch(Exception e) {
			logger.error("Exception while fetching slack token", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value = "/slack", method = RequestMethod.PUT)
	@ApiOperation(value = "Update slack auth token")
	public ResponseEntity<Map<String, Object>> updateSlackToken(@RequestBody @Valid SlackTokenDTO slackToken) {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.status(HttpStatus.BAD_REQUEST).
				body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, ResponseMessages.SLACK_TOKEN_INVALID).getResponse());
		try {
			boolean isValid = slackService.isTokenValid(slackToken.getToken());
			if (isValid) {
				configurationService.updateSlackToken(slackToken.getToken());
				result = ResponseEntity.ok().body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, ResponseMessages.SLACK_TOKEN_CONFIGURATION_UPDATED).getResponse());
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
		ResponseEntity<Map<String, Object>> result = ResponseEntity.status(HttpStatus.BAD_REQUEST).
				body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, HttpStatus.BAD_REQUEST.getReasonPhrase()).getResponse());
		try {
			ResponseDTO responseDTO = new ResponseDTO(ZvisitorResource.EMAIL.toLowerCase(), eMailConfigConverter.convertToDTO(configurationService.getMailConfiguration()));
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
		ResponseEntity<Map<String, Object>> result = ResponseEntity.status(HttpStatus.BAD_REQUEST).
				body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, ResponseMessages.EMAIl_CONFIG_INVALID).getResponse());
		try {
			Email mailConfiguration = eMailConfigConverter.convert(mailConfigurationDTO);
			emailService.authenticateAndLoadConfiguration(mailConfiguration);
			configurationService.updateMailConfig(mailConfiguration);
			result = ResponseEntity.ok().body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, ResponseMessages.EMAIL_CONFIGURATION_UPDATED).getResponse());
		} catch(AuthenticationFailedException | MailConnectException e) {
			result = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, ResponseMessages.EMAIl_CONFIG_INVALID).getResponse());
			logger.error("Exception while updating email configuration.", e);
		} catch(Exception e) {
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			logger.error("Exception while updating email configuration.", e);
		}
		return result;
	}

	@RequestMapping(value = "/location", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "add location", response = ResponseDTO.class)
	public ResponseEntity<Map<String, Object>> addLocation(@RequestBody @Valid LocationDTO locationDTO) {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.status(HttpStatus.BAD_REQUEST).
				body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, HttpStatus.BAD_REQUEST.getReasonPhrase()).getResponse());
		try {
			Location location = locationConverter.convert(locationDTO);
			locationService.save(location);
			result = ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, ResponseMessages.LOCATION_ADDED_SUCCESSFLLY).getResponse());
		} catch(DuplicateKeyException e) {
			logger.error("Location already exists.", e);
			result = ResponseEntity.badRequest().body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, ResponseMessages.LOCATION_EXISTS).getResponse());
		} catch(Exception e) {
			logger.error("Exception while updating location.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value = "/location/{locId}", method = RequestMethod.PUT)
	@ApiOperation(value = "update location", response = ResponseDTO.class)
	public ResponseEntity<Map<String, Object>> updateLocation(@RequestBody @Valid LocationDTO locationDTO, @PathVariable @NotBlank String locId) {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.status(HttpStatus.BAD_REQUEST).
				body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, HttpStatus.BAD_REQUEST.getReasonPhrase()).getResponse());
		try {
			Location location = locationConverter.convert(locationDTO);
			locationService.update(locId, location);
			result = ResponseEntity.ok().body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, ResponseMessages.LOCATION_CONFIGURATION_CONFIGURATION_UPDATED).getResponse());
		} catch(DuplicateKeyException e) {
			logger.error("Location already exists.", e);
			result = ResponseEntity.badRequest().body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, ResponseMessages.LOCATION_EXISTS).getResponse());
		} catch(NoDataFoundException e) {
			logger.error("Exception while updating location.", e);
			result = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, ResponseMessages.LOCATION_NOT_FOUND).getResponse());
		} catch(Exception e) {
			logger.error("Exception while updating location.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value ="/location/{locId}", method = RequestMethod.DELETE)
	@ApiOperation(value = "delete location", response = ResponseDTO.class)
	public ResponseEntity<Map<String, Object>> deleteLocation(@PathVariable("locId") @NotBlank String id) {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.status(HttpStatus.BAD_REQUEST).
				body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, HttpStatus.BAD_REQUEST.getReasonPhrase()).getResponse());
		try {
			locationService.delete(id);
			result = ResponseEntity.ok().body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, ResponseMessages.LOCATION_DELETED_SUCCESSFULLY).getResponse());
		} catch (Exception e) {
			logger.error("Exception while deleting location.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value = "/channels", method=RequestMethod.POST)
	@ApiOperation(value = "add channel", response = ResponseDTO.class)
	public ResponseEntity<Map<String, Object>> addChannel(@RequestPart("icon") MultipartFile file, @RequestParam(value="channel", required=true) String channelJson)  {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.status(HttpStatus.BAD_REQUEST).
				body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, HttpStatus.BAD_REQUEST.getReasonPhrase()).getResponse());
		try {
			SlackChannelDTO channelDTO = JsonUtils.fromJson(channelJson, SlackChannelDTO.class);
			if (Objects.nonNull(channelDTO)) {
				SlackChannel channel = slackChannelConverter.convert(channelDTO);
				channelService.save(file, channel);
			}
			result = ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, ResponseMessages.SLACK_CHANNEL_ADDED_SUCCESSFLLY).getResponse());
		}  catch(DuplicateKeyException e) {
			logger.error("Exception while adding channel.", e);
			result = ResponseEntity.badRequest().body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, ResponseMessages.SLACK_DUPLICATE_ID_RESPONSE).getResponse());
		}  catch(IOException | InvalidDataException e) {
			logger.error("Exception while adding channel.", e);
		}  catch (Exception e) {
			logger.error("Exception while adding channel.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value = "/channels/{chId}", method=RequestMethod.PUT)
	@ApiOperation(value = "update channel", response = ResponseDTO.class)
	public ResponseEntity<Map<String, Object>> updateChannel(@RequestPart(value="icon",  required=false) MultipartFile file, @RequestParam(value="channel", required=true) String channelJson, @PathVariable String chId) {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.status(HttpStatus.BAD_REQUEST).
				body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, HttpStatus.BAD_REQUEST.getReasonPhrase()).getResponse());
		try {
			SlackChannelDTO channelDTO = JsonUtils.fromJson(channelJson, SlackChannelDTO.class);
			if (Objects.nonNull(channelDTO) && StringUtils.isNotBlank(chId)) {
				SlackChannel channel = slackChannelConverter.convert(channelDTO);
				channelService.update(file, channel, chId);
				result = ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, ResponseMessages.SLACK_CHANNEL_CONFIGURATION_UPDATED).getResponse());
			}
		}  catch(DuplicateKeyException e) {
			logger.error("Exception while adding channel.", e);
			result = ResponseEntity.badRequest().body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, ResponseMessages.SLACK_DUPLICATE_ID_RESPONSE).getResponse());
		}  catch(IOException e) {
			logger.error("Exception while Json parsing", e);
		}  catch(NoDataFoundException e) {
			logger.error("Exception while updating channel", e);
			result = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, ResponseMessages.SLACK_INVALID_RESPONSE).getResponse());
		}  catch(Exception e) {
			logger.error("Exception while updating channel.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value ="/channels/{id}", method=RequestMethod.DELETE)
	@ApiOperation(value = "delete channel", response = ResponseDTO.class)
	public ResponseEntity<Map<String, Object>> deleteChannel(@PathVariable("id") String id) {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.status(HttpStatus.BAD_REQUEST).
				body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, HttpStatus.BAD_REQUEST.getReasonPhrase()).getResponse());
		try {
			channelService.delete(id);
			result = ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, ResponseMessages.SLACK_CHANNEL_CONFIGURATION_CONFIGURATION_DELETED).getResponse());
		}  catch (InvalidDataException e) {
			logger.error("Exception while deleting channel.", e);
			result = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(ResponseMessages.RESPONSE_MESSAGE_KEY, ResponseMessages.SLACK_INVALID_RESPONSE).getResponse());
		}  catch (Exception e) {
			logger.error("Exception while deleting channel.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}
}
