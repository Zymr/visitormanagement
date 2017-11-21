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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.mime.content.ContentBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.connectors.HttpConnector;
import com.zymr.zvisitor.connectors.HttpConnectorHelper;
import com.zymr.zvisitor.dto.slack.Channels;
import com.zymr.zvisitor.dto.slack.Response;
import com.zymr.zvisitor.dto.slack.SlackEmployee;
import com.zymr.zvisitor.util.Constants;
import com.zymr.zvisitor.util.JsonUtils;
import com.zymr.zvisitor.util.enums.NotificationKey;

@Service
@Configuration
public class SlackService {
	private static final Logger logger = LoggerFactory.getLogger(SlackService.class);

	@Autowired
	private HttpConnector httpConnector;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private ImageService imageService;

	@PostConstruct
	public void init() {
		logger.info("Slack Service {}" , toString());
	}

	/**
	 * 
	 * @param token
	 * 
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public boolean isTokenValid(String token) throws ClientProtocolException, IOException {
		Map<String, String> map = new HashMap<>();
		map.put(NotificationKey.token.name(), token);
		HttpResponse httpResponse = httpConnector.postRequest(HttpConnectorHelper.buildEntityWithBodyParam(map), Constants.SLACK_AUTH_TEST);
		Response responseMembers = JsonUtils.fromJson((HttpConnectorHelper.fromResponseToString(httpResponse)), Response.class);
		return responseMembers.isOk();
	}

	/**
	 * This method is used to get list of employees from slack.
	 * 
	 * @return List of slackEmployees. 
	 */
	public List<SlackEmployee> getEmployeeList() throws IOException {
		Map<String, String> map = new HashMap<>();
		map.put(NotificationKey.token.name(), configurationService.getUpdatedToken());
		HttpResponse httpResponse = httpConnector.postRequest(HttpConnectorHelper.buildEntityWithBodyParam(map), Constants.USER_LIST_API);
		Response responseMembers = JsonUtils.fromJson((HttpConnectorHelper.fromResponseToString(httpResponse)), Response.class);
		if (Objects.nonNull(responseMembers.getMembers())) {
			return responseMembers.getMembers();
		}
		return null;
	}

	/**
	 * This method is used to get channel details from slack.
	 * 
	 * @param channelIds List of channelId for which channel details is needed.
	 * 
	 * @return List of slack channels.
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public List<Channels> getChannelList(Map<String,String> channelIds) throws ClientProtocolException, IOException {
		List<Channels> slackChannels = new ArrayList<>();
		for (Map.Entry<String, String> channelId : channelIds.entrySet()) {
			Map<String, String> param = buildRequestForChannelInfo(channelId.getKey(), configurationService.getUpdatedToken());
			HttpResponse response =  httpConnector.postRequest(HttpConnectorHelper.buildEntityWithBodyParam(param), Constants.GROUP_INFO_API);
			Response responseMembers = JsonUtils.fromJson((HttpConnectorHelper.fromResponseToString(response)), Response.class);
			if (Objects.nonNull(responseMembers.getGroup())) {
				Channels slackChannel = responseMembers.getGroup();
				slackChannel.setEmail(channelId.getValue());
				slackChannels.add(slackChannel);
			}
		}
		return slackChannels;
	}

	/**
	 * This method is used to get employees of specific channel.
	 * 
	 * @param channelId channelId to get member list.
	 * 
	 * @return List of members.d
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public Set<String> getEmployeesOfChannel(String channelId) throws ClientProtocolException, IOException {
		Map<String, String> param = buildRequestForChannelInfo(channelId, configurationService.getUpdatedToken());	
		HttpResponse response =  httpConnector.postRequest(HttpConnectorHelper.buildEntityWithBodyParam(param), Constants.CHANNEL_INFO_API);
		Response responseMembers = JsonUtils.fromJson((HttpConnectorHelper.fromResponseToString(response)), Response.class);
		if (Objects.nonNull(responseMembers.getChannel())) {
			return responseMembers.getChannel().getMembers();
		}
		return null;
	}


	/**used to send slack notification.
	 * 
	 * @param param
	 * @param attachment
	 */
	public void sendMessage(Map<String, String> param, Map<String, ContentBody> attachment) {
		try {
			CloseableHttpResponse res = httpConnector.postRequest(HttpConnectorHelper.buildMultiPartEntity(param, attachment), Constants.SLACK_FILE_SEND_API);
			logger.info(res.toString());
		} catch(Exception e) {
			logger.error("Exception while sending slack notifaction.",e);
		}
	}

	/** To create body for slack channel info.
	 * @param channelId
	 * @param token
	 * @return
	 */
	public Map<String, String> buildRequestForChannelInfo(String channelId, String token) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put(NotificationKey.token.name(), token);
		parameters.put(NotificationKey.channel.name(), channelId);
		return parameters;
	}

	@Override
	public String toString() {
		return "SlackService [httpConnector=" + httpConnector 
				+ ", configurationService=" + configurationService + ", imageService=" + imageService + "]";
	}
}
