/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/

package com.zymr.zvisitor.service.config;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.zymr.zvisitor.exception.InvalidDataException;
import com.zymr.zvisitor.util.Constants;

/**
 * @author chirag.b
 *
 */
@Component
@ConfigurationProperties(ignoreUnknownFields = true , prefix ="app") 
public class AppProperties {
	
	private static final Logger logger = LoggerFactory.getLogger(AppProperties.class);
	
	@Autowired
	private MailProperties mailProperties;

	@Autowired
	private MongoProperties mongoProperties;

	@Value("${server.address:localhost}")
	private String serverAddress;

	@Value("${server.port:8080}")
	private Integer serverPort;

	private String baseUrl;
	
	@NotBlank
	private String mailPersonal;
	
	@NotBlank
	private String empSyncJob;

	@NotNull
	private SlackAccountConfig slack;

	@NotNull
	private AppConfig config;
	
	@NotNull
	private Map<String, String> visitorCategory;
	
	@NotNull
	private Organization org;

	@NotBlank
	private String adminEmail;
	
	@NotBlank
	private String adminPassword;

	@NotBlank
	private String secretKey;

	@PostConstruct
	public void init() throws InvalidDataException, IOException {
		validateProperties();
		logger.info("Service {}" , toString());
	}

	private void validateProperties() throws InvalidDataException {
		//Emmployee slack message
		if (StringUtils.isBlank(slack.getMessage())) {
			String data = null;
			try {
				data = IOUtils.toString(AppProperties.class.getClassLoader().getResourceAsStream(Constants.SLACK_TEMPLATE), Charset.defaultCharset());
			} catch (IOException e) {
				throw new InvalidDataException("Exception while fetching empployee slack message template." ,e);	
			}
			slack.setMessage(data);
		}

		//Group slack message
		if (StringUtils.isBlank(slack.getChannelmessage())) {
			String channelMessage = null;
			try {
				channelMessage = IOUtils.toString(AppProperties.class.getClassLoader().getResourceAsStream(Constants.SLACK_CHANNEL_TEMPLATE), Charset.defaultCharset());
			} catch (IOException e) {
				throw new InvalidDataException("Exception while fetching group slack message template." ,e);	
			}
			slack.setChannelmessage(channelMessage);
		}

		if (StringUtils.isBlank(baseUrl)) {
			baseUrl = "http://"+serverAddress+":"+serverPort+"";
		}

		if (Objects.isNull(mailProperties) || StringUtils.isBlank(mailProperties.getHost()) || mailProperties.getPort() <= 0
				|| StringUtils.isBlank(mailProperties.getUsername()) || StringUtils.isBlank(mailProperties.getPassword())) {
			throw new InvalidDataException("Mail property values not injected properly.");
		}

		if (Objects.isNull(mongoProperties) || StringUtils.isBlank(mongoProperties.getDatabase()) || mongoProperties.getPort() <= 0 || StringUtils.isBlank(mongoProperties.getHost())) {
			throw new InvalidDataException("Mongo property values not injected properly.");
		}

		if (StringUtils.isBlank(baseUrl) || StringUtils.isBlank(mailPersonal) || StringUtils.isBlank(empSyncJob)) {
			throw new InvalidDataException("Application Property file values not injected properly.");
		}

		if (Objects.isNull(slack) ||StringUtils.isBlank(slack.getToken()) || StringUtils.isBlank(slack.getMessage()) || 
				StringUtils.isBlank(slack.getUserName()) || StringUtils.isBlank(slack.getChannelmessage())) {
			throw new InvalidDataException("Slack property values not injected properly.");
		}

		if (Objects.isNull(config) ||StringUtils.isBlank(config.getCategoryImagesPath()) || StringUtils.isBlank(config.getDefaultImagePath()) || 
				StringUtils.isBlank(config.getDepartmentImagesPath()) || StringUtils.isBlank(config.getFileUploadsPath())) {
			throw new InvalidDataException("Application Files configuration property values not injected properly.");
		}

		if (StringUtils.isBlank(adminEmail) || StringUtils.isBlank(adminPassword)) {
			throw new InvalidDataException("Admin Email or password can not be null.");
		}

		if(StringUtils.isBlank(secretKey))
			throw new InvalidDataException("Secret Key can not be null.");
	}

	public MailProperties getMailProperties() {
		return mailProperties;
	}

	public void setMailProperties(MailProperties mailProperties) {
		this.mailProperties = mailProperties;
	}

	public MongoProperties getMongoProperties() {
		return mongoProperties;
	}

	public void setMongoProperties(MongoProperties mongoProperties) {
		this.mongoProperties = mongoProperties;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public Integer getServerPort() {
		return serverPort;
	}

	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getMailPersonal() {
		return mailPersonal;
	}

	public void setMailPersonal(String mailPersonal) {
		this.mailPersonal = mailPersonal;
	}

	public String getEmpSyncJob() {
		return empSyncJob;
	}

	public void setEmpSyncJob(String empSyncJob) {
		this.empSyncJob = empSyncJob;
	}

	public SlackAccountConfig getSlack() {
		return slack;
	}

	public void setSlack(SlackAccountConfig slack) {
		this.slack = slack;
	}

	public AppConfig getConfig() {
		return config;
	}

	public void setConfig(AppConfig config) {
		this.config = config;
	}

	public Map<String, String> getVisitorCategory() {
		return visitorCategory;
	}

	public void setVisitorCategory(Map<String, String> visitorCategory) {
		this.visitorCategory = visitorCategory;
	}

	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
	
	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	@Override
	public String toString() {
		return "AppProperties [mailProperties=" + mailProperties + ", mongoProperties=" + mongoProperties
				+ ", serverAddress=" + serverAddress + ", serverPort=" + serverPort + ", baseUrl=" + baseUrl
				+ ", mailPersonal=" + mailPersonal + ", empSyncJob=" + empSyncJob + ", slack=" + slack + ", config="
				+ config + ", visitorCategory=" + visitorCategory + ", org=" + org + ", adminEmail=" + adminEmail
				+ ", secretKey=" + secretKey + "]";
	}
}
