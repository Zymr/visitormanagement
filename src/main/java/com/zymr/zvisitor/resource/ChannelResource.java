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

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zymr.zvisitor.converter.SlackChannelConverter;
import com.zymr.zvisitor.dbo.SlackChannel;
import com.zymr.zvisitor.dto.ResponseDTO;
import com.zymr.zvisitor.dto.SlackChannelDTO;
import com.zymr.zvisitor.service.ChannelService;
import com.zymr.zvisitor.util.Constants;
import com.zymr.zvisitor.util.enums.ZvisitorResource;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(Constants.CHANNEL_URL)
public class ChannelResource extends BaseResource {
	private static final Logger logger = LoggerFactory.getLogger(ChannelResource.class);

	@Autowired
	private ChannelService channelService;

	@Autowired
	private SlackChannelConverter slackChannelConverter;

	@RequestMapping(method=RequestMethod.GET)
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

	@RequestMapping(value="/syncChannels", method=RequestMethod.GET) 
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
}
