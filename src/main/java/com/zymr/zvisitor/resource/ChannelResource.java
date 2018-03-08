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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zymr.zvisitor.converter.SlackChannelConverter;
import com.zymr.zvisitor.dbo.SlackChannel;
import com.zymr.zvisitor.dto.ResponseDTO;
import com.zymr.zvisitor.dto.SlackChannelDTO;
import com.zymr.zvisitor.exception.InvalidDataException;
import com.zymr.zvisitor.exception.NoDataFoundException;
import com.zymr.zvisitor.service.ChannelService;
import com.zymr.zvisitor.util.Constants;
import com.zymr.zvisitor.util.JsonUtils;
import com.zymr.zvisitor.util.enums.ZvisitorResource;

import io.swagger.annotations.ApiOperation;

@RestController
public class ChannelResource {
	private static final Logger logger = LoggerFactory.getLogger(ChannelResource.class);

	@Autowired
	private ChannelService channelService;

	@Autowired
	private SlackChannelConverter slackChannelConverter;

	@RequestMapping(value=Constants.CHANNEL_URL, method=RequestMethod.GET)
	@ApiOperation(value = "Fetch all channels", response = ResponseDTO.class)
	public ResponseEntity<Map<String, Object>> get() {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.notFound().build();
		try {
			List<SlackChannel> channel = channelService.get();
			Collection<SlackChannelDTO> channelsDTO = slackChannelConverter.convertToDTO(channel);
			if (CollectionUtils.isNotEmpty(channelsDTO)) {
				ResponseDTO responseDTO = new ResponseDTO(ZvisitorResource.CHANNELS.toLowerCase(), channelsDTO);
				result = ResponseEntity.ok(responseDTO.getResponse());
			} 
		} catch(Exception e) {
			logger.error("Exception while fetching all channels.", e);
			result =  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value=Constants.SYNC_CHANNEL, method=RequestMethod.GET) 
	public ResponseEntity<Map<String, Object>> syncChannels() {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.badRequest().build();
		try {
			channelService.syncChannelsFromSlack();
			result = ResponseEntity.ok().build(); 
		} catch (Exception e) {
			logger.error("Exception while syncing channel.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result; 
	}

	@RequestMapping(value = Constants.AUTH_CHANNEL_URL, method=RequestMethod.POST)
	@ApiOperation(value = "add channel", response = ResponseDTO.class)
	public ResponseEntity<Map<String, Object>> addChannel(@RequestPart("icon") MultipartFile file, @RequestParam(value="channel", required=true) String channelJson)  {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.badRequest().build();
		try {
			SlackChannelDTO channelDTO = JsonUtils.fromJson(channelJson, SlackChannelDTO.class);
			if (Objects.nonNull(channelDTO)) {
				SlackChannel channel = slackChannelConverter.convert(channelDTO);
				channelService.save(file, channel);
			}
			result = ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(Constants.RESPONSE_MESSAGE_KEY, Constants.SLACK_CHANNEL_ADDED_SUCCESSFLLY).getResponse());
		}  catch(DuplicateKeyException e) {
			logger.error("Exception while adding channel.", e);
			result = ResponseEntity.badRequest().body(new ResponseDTO(Constants.RESPONSE_MESSAGE_KEY, Constants.SLACK_DUPLICATE_ID_RESPONSE).getResponse());
		}  catch(IOException | InvalidDataException e) {
			logger.error("Exception while adding channel.", e);
		}  catch (Exception e) {
			logger.error("Exception while adding channel.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value = Constants.CHANNEL_UPDATE_DELETE_URL, method=RequestMethod.PUT)
	@ApiOperation(value = "update channel", response = ResponseDTO.class)
	public ResponseEntity<Map<String, Object>> updateChannel(@RequestPart(value="icon",  required=false) MultipartFile file, @RequestParam(value="channel", required=true) String channelJson, @PathVariable String chId) {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.badRequest().build();
		try {
			SlackChannelDTO channelDTO = JsonUtils.fromJson(channelJson, SlackChannelDTO.class);
			if (Objects.nonNull(channelDTO) && StringUtils.isNotBlank(chId)) {
				SlackChannel channel = slackChannelConverter.convert(channelDTO);
				channelService.update(file, channel, chId);
				result = ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(Constants.RESPONSE_MESSAGE_KEY, Constants.SLACK_CHANNEL_CONFIGURATION_CONFIGURATION_UPDATED).getResponse());
			}
		}  catch(DuplicateKeyException e) {
			logger.error("Exception while adding channel.", e);
			result = ResponseEntity.badRequest().body(new ResponseDTO(Constants.RESPONSE_MESSAGE_KEY, Constants.SLACK_DUPLICATE_ID_RESPONSE).getResponse());
		}  catch(IOException e) {
			logger.error("Exception while Json parsing", e);
		}  catch(NoDataFoundException e) {
			logger.error("Exception while updating channel", e);
			result = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}  catch(Exception e) {
			logger.error("Exception while updating channel.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

	@RequestMapping(value =Constants.CHANNEL_UPDATE_DELETE_URL, method=RequestMethod.DELETE)
	@ApiOperation(value = "delete channel", response = ResponseDTO.class)
	public ResponseEntity<Map<String, Object>> deleteChannel(@PathVariable("chId") String id) {
		ResponseEntity<Map<String, Object>> result = ResponseEntity.badRequest().build();
		try {
			channelService.delete(id);
			result = ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(Constants.RESPONSE_MESSAGE_KEY, Constants.SLACK_CHANNEL_CONFIGURATION_CONFIGURATION_DELETED).getResponse());
		}  catch (NoDataFoundException e) {
			logger.error("Exception while deleting channel.", e);
			result = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}  catch (Exception e) {
			logger.error("Exception while deleting channel.", e);
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return result;
	}

}
