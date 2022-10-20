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
import com.zymr.zvisitor.dto.slack.Conversation;
import com.zymr.zvisitor.dto.slack.Response;
import com.zymr.zvisitor.dto.slack.SlackEmployee;
import com.zymr.zvisitor.service.config.SlackChannelConfig;
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
		map.put(NotificationKey.TOKEN.toLowerCase(), token);
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
//		map.put(NotificationKey.TOKEN.toLowerCase(), configurationService.getUpdatedToken());
		HttpResponse httpResponse = httpConnector.postRequest(HttpConnectorHelper.buildEntityWithBodyParam(map),
				getAuthHeader(configurationService.getUpdatedToken()),
				Constants.USER_LIST_API);
		String slackResponse = HttpConnectorHelper.fromResponseToString(httpResponse);
		logger.info("Slack Employee List {}" , slackResponse);
		Response responseMembers = JsonUtils.fromJson(slackResponse, Response.class);
		if (Objects.nonNull(responseMembers.getMembers())) {
			return responseMembers.getMembers();
		}
		return null;
	}

	/**
	 * This method is used to get channel details from slack.
	 * 
	 * @return List of slack channels.
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public List<Channels> getChannelList(List<SlackChannelConfig> department) throws ClientProtocolException, IOException {
		
		List<Channels> slackChannels = new ArrayList<>();
		
		for (SlackChannelConfig slackChannelConfig : department) {
			Map<String, String> param = buildRequestForChannelInfo(slackChannelConfig.getSlackid());
			HttpResponse response =  httpConnector.postRequest(HttpConnectorHelper.buildEntityWithBodyParam(param),
					getAuthHeader(configurationService.getUpdatedToken()),
					Constants.CONVERSATIONS_INFO_API);
			logger.info("HTTP Response getChannelList: ", response);
			Response responseMembers = JsonUtils.fromJson((HttpConnectorHelper.fromResponseToString(response)), Response.class);
			if (Objects.nonNull(responseMembers.getGroup())) {
				Channels slackChannel = responseMembers.getChannel();
				slackChannel.setEmail(slackChannelConfig.getEmail());
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
	public Set<String> getEmployeesOfChannel(final String channelId) throws ClientProtocolException, IOException {
		final Map<String, String> param = buildRequestForChannelInfo(channelId);
		final HttpResponse response =  httpConnector.postRequest(HttpConnectorHelper.buildEntityWithBodyParam(param),
				getAuthHeader(configurationService.getUpdatedToken()),
				Constants.CONVERSATIONS_MEMBERS_API);
		logger.info("Slack: get conversation members APII response", response);
		final Conversation conversation = JsonUtils.fromJson((HttpConnectorHelper.fromResponseToString(response)), Conversation.class);
		logger.info("Slack: get conversation member list", response);
		return conversation.getMembers();
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
	 * @return
	 */
	public Map<String, String> buildRequestForChannelInfo(String channelId) {
		final Map<String, String> parameters = new HashMap<>();
		parameters.put(NotificationKey.CHANNEL.toLowerCase(), channelId);
		return parameters;
	}

	public Map<String, String> getAuthHeader(final String token) {
		return Map.of("Authorization", "Bearer "+token);
	}

	@Override
	public String toString() {
		return "SlackService [httpConnector=" + httpConnector 
				+ ", configurationService=" + configurationService + ", imageService=" + imageService + "]";
	}
}
