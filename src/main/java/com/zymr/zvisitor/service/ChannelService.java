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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zymr.zvisitor.converter.ChannelConverter;
import com.zymr.zvisitor.dbo.SlackChannel;
import com.zymr.zvisitor.dto.slack.Channels;
import com.zymr.zvisitor.exception.InvalidDataException;
import com.zymr.zvisitor.exception.NoDataFoundException;
import com.zymr.zvisitor.repository.ChannelRepository;
import com.zymr.zvisitor.service.config.AppProperties;
import com.zymr.zvisitor.service.config.SlackChannelConfig;
import com.zymr.zvisitor.util.Constants;
import com.zymr.zvisitor.util.enums.ImageType;

@Service
public class ChannelService {
	private static final Logger logger = LoggerFactory.getLogger(ChannelService.class);

	@Autowired 
	private ChannelRepository channelRepository;

	@Autowired
	private SlackService slackService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private ChannelConverter channelConverter;

	@Autowired
	private AppProperties appProperties;

	/**
	 * To sync channels with slack and insert channels into database.
	 *  
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * 
	 * @throws Exception
	 */
	public void syncChannelsFromSlack() throws ClientProtocolException, IOException {
		long channelCount = channelRepository.count();
		List<SlackChannelConfig> department = appProperties.getOrg().getDepartment();
		if (channelCount == 0  && channelCount != department.size() && CollectionUtils.isNotEmpty(department)) {
			logger.info("Syncing of channels from slack started. channels {}", department);
			List<Channels> slackChannels = slackService.getChannelList(department); 
			List<SlackChannel> channel =  slackChannels.stream().map(c -> convertToDTO(c))
																.collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(channel)) {
				channelRepository.save(channel);
			}
		}
	}

	/**
	 * To save channel into database.
	 * 
	 * @param MultipartFile file to upload.
	 * @param SlackChannel
	 * 
	 * @throws IOException
	 * @throws InvalidDataException 
	 */ 
	public void save(MultipartFile icon, SlackChannel channel) throws DuplicateKeyException, IOException, InvalidDataException {
		if	(Objects.isNull(channel))	{
			throw new InvalidDataException(Constants.INVALID_PARAM);
		}
		uploadFile(icon);	
		channel.setImageSmall(imageService.getImageUrl(ImageType.DEPARTMENT, icon.getOriginalFilename()));
		channelRepository.save(channel);
	}

	/**
	 * To update channel.
	 * 
	 * @param MultipartFile file to upload.
	 * @param SlackChannel
	 * @param id channel id that need to be update
	 * @throws IOException 
	 * @throws NoDataFoundException 
	 */ 
	public void update(MultipartFile icon, SlackChannel channel, String id) throws IOException, NoDataFoundException {
		SlackChannel dbChannel = getById(id);
		if	(Objects.isNull(dbChannel))	{
			throw new NoDataFoundException(Constants.INVALID_PARAM);
		}
		if (Objects.nonNull(channel)) {
			dbChannel.setChannelId(channel.getChannelId());
			dbChannel.setEmail(channel.getEmail());
			dbChannel.setName(channel.getName());
		}
		if (Objects.nonNull(icon)) {
			uploadFile(icon);	
			String channelIconPath = imageService.getImageUrl(ImageType.DEPARTMENT, icon.getOriginalFilename());
			dbChannel.setImageSmall(channelIconPath);
		}
		this.channelRepository.save(dbChannel);
	}

	/**
	 * To get all channels from database.
	 * 
	 * @return List of channels.
	 */ 
	public List<SlackChannel> get() {
		return this.channelRepository.findAll();
	}

	/**
	 * @param id
	 * @throws NoDataFoundException 
	 */
	public void delete(String id) throws NoDataFoundException {
		long count = countById(id);
		if (count <= 0) {
			throw new NoDataFoundException(Constants.NO_DATA_FOUND);
		}
		channelRepository.delete(id);
	}

	/**
	 * @param id
	 * @return Channel
	 */
	public SlackChannel getById(String id) {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		return channelRepository.findById(id);
	}

	/**
	 * @param id
	 * @return Channel
	 */
	public long countById(String id) {
		if (StringUtils.isBlank(id)) {
			return 0;
		}
		return channelRepository.countById(id);
	}

	/**
	 * To get channel by specific channel id from database.
	 * 
	 * @param channelId 
	 * @return Channel.
	 */ 
	public SlackChannel findByChannelId(String channelId) {
		if (StringUtils.isBlank(channelId)) {
			return null;
		}
		return channelRepository.findByChannelId(channelId);
	}


	/**
	 * To upload channel icon.
	 * 
	 * @param MultipartFile file to upload.
	 * 
	 * @throws IOException
	 */ 
	private void uploadFile(MultipartFile icon) throws IOException {
		if (Objects.nonNull(icon)) {
			icon.transferTo(new File(imageService.createFileUploadPath(ImageType.DEPARTMENT, icon.getOriginalFilename())));
		}
	} 

	/**
	 * @param channel
	 * @return SlackChannel
	 */
	private SlackChannel convertToDTO(Channels channel) {
		return channelConverter.convertToDTO(channel, imageService.getImageUrl(ImageType.DEPARTMENT, channel.getName())+Constants.IMAGE_EXT);
	}
}
