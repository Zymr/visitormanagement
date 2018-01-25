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

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.dbo.SlackChannel;
import com.zymr.zvisitor.dto.slack.Channels;
import com.zymr.zvisitor.service.config.AppProperties;

@Service
public class ChannelConverter implements Converter<Channels, SlackChannel> {

	@Autowired
	private AppProperties appProperties;
	
	@Override
	public SlackChannel convertToDTO(Channels slackChannel) {
		if (Objects.isNull(slackChannel)) {
			return null;
		}
		return new SlackChannel(slackChannel.getChannelId(), slackChannel.getName(),
				slackChannel.getEmail(), appProperties.getBaseUrl());
	}

	public SlackChannel convertToDTO(Channels slackChannel, String baseUrl) {
		if (Objects.isNull(slackChannel)) {
			return null;
		}
		return new SlackChannel(slackChannel.getChannelId(), slackChannel.getName(),
				slackChannel.getEmail(), baseUrl );
	}

	@Override
	public Collection<SlackChannel> convertToDTO(Collection<Channels> s) {
		throw new NotImplementedException(ChannelConverter.class);
	}

	@Override
	public Channels convert(SlackChannel d) {
	  throw new NotImplementedException(ChannelConverter.class);
	}

	@Override
	public Collection<Channels> convert(Collection<SlackChannel> d) {
	  throw new NotImplementedException(ChannelConverter.class);
	}
}
