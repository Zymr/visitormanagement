/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.converter;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.dbo.SlackChannel;
import com.zymr.zvisitor.dto.SlackChannelDTO;
import com.zymr.zvisitor.service.ConfigurationService;
import com.zymr.zvisitor.util.Util;

@Service
public class SlackChannelConverter implements Converter<SlackChannel, SlackChannelDTO> {
	
	@Autowired
	private ConfigurationService configurationService;

	@Override
	public SlackChannelDTO convertToDTO(SlackChannel channel) {
		if (Objects.isNull(channel)) {
			return null;
		}
		return new SlackChannelDTO(channel.getId(), channel.getChannelId(), 
				channel.getName(), channel.getEmail(), Util.buildURL(configurationService.getBaseUrl(), channel.getImageSmall()));
	}

	@Override
	public Collection<SlackChannelDTO> convertToDTO(Collection<SlackChannel> channels) {
		if (CollectionUtils.isEmpty(channels)) {
			return null;
		}
		return channels.stream().filter(Objects::nonNull)
				.map(channel -> convertToDTO(channel))
				.collect(Collectors.toList());
	}

	@Override
	public SlackChannel convert(SlackChannelDTO channelDTO) {
		if (Objects.isNull(channelDTO)) {
			return null;
		}
		return new SlackChannel(channelDTO.getChannelId(), channelDTO.getFullName(),
				channelDTO.getEmail(), channelDTO.getImageSmall());
	}

	@Override
	public Collection<SlackChannel> convert(Collection<SlackChannelDTO> d) {
		throw new NotImplementedException(SlackChannelConverter.class);
	}
}
