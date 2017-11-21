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

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.dbo.config.Email;
import com.zymr.zvisitor.dbo.config.PropertyConfiguration;
import com.zymr.zvisitor.dbo.config.PropertyConfiguration.CONFIGURATION_FIELDS;
import com.zymr.zvisitor.dbo.config.Slack;
import com.zymr.zvisitor.exception.InvalidDataException;
import com.zymr.zvisitor.repository.ConfigurationRepository;

@Service
public class ConfigurationService {
	private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);

	@Autowired
	private ConfigurationRepository configurationRepository;

	@Value("${spring.mail.host}")
	private String host; 

	@Value("${spring.mail.port}")
	private int port; 

	@Value("${spring.mail.username}")
	private String name;

	@Value("${spring.mail.password}")
	private String password;

	@Value("${mail.from}")
	private String from;

	@Value("${slack.userName}")
	private String userName;

	@Value("${slack.message}")
	private String message;

	@Value("${slack.channelmessage}")
	private String channelmessage;

	@Value("${slack.token}")
	private String token;
	
	@Value("#{${zymr.department}}")
	private Map<String,String> channels;
	
	@Value("#{'${valid.email.domain}'.split(',')}")
	private List<String> validEmailList;
	
	@Value("#{${location}}")
	private Map<String,Map<String, String>> locations;
	
	@Value("#{${visitor.category}}")
	private Map<String,String> visitorOrigins;
	
	@Value("${config.categoryImagesPath}")
	private String categoryImagesPath;

	@Value("${config.departmentImagesPath}")
	private String departmentImagesPath;

	@Value("${config.fileBaseDir}")
	private String fileBaseDir;

	@Value("${config.fileUploadsPath}")
	private String fileUploadsPath; 
	
	@Value("${config.defaultImagePath}")
	private String defaultImagePath;
	
	@Value("${config.ndaFile}")
	private String ndaFile;

	private String baseUrl;

	private Slack slackConfiguration;
	
	private Email mailConfiguration;

	@PostConstruct
	public void init() throws InvalidDataException {
		if (configurationRepository.count() > 0) {
			loadConfiguration();
		} else {
			loadAndUpdateDbConfiguration();
		}
		logger.info("Configuration Service", toString());
	}

	/** To save properties configuration into database. 
	 * 
	 * @throws InvalidDataException */
	public void loadAndUpdateDbConfiguration() throws InvalidDataException {
		this.propertyValidate();
		this.slackConfiguration = new Slack(token, userName, message, channelmessage);
		this.mailConfiguration = new Email(host, port, name, password, from);
		PropertyConfiguration propertyConfiguration = new PropertyConfiguration(slackConfiguration, mailConfiguration);
		configurationRepository.save(propertyConfiguration);
		logger.info("Property configuration saved.",  propertyConfiguration.toString());
	}

	/**
	 * To update slack authentication token.
	 * 
	 * @param slackToken
	 */
	public void updateSlackToken(String slackToken) {
		PropertyConfiguration propertyConfiguration = getConfigurationFromDB();
		slackConfiguration = propertyConfiguration.getSlackConfig();
		slackConfiguration.setToken(slackToken);
		propertyConfiguration.setSlackConfig(slackConfiguration);
		configurationRepository.save(propertyConfiguration);
		logger.info("Slack configuration updated.", slackConfiguration.toString());
	}

	/**
	 * To update mail configuration.
	 * 
	 * @param Email
	 */
	public void updateMailConfig(Email emailConfig) {
		PropertyConfiguration propertyConfiguration = getConfigurationFromDB();
		propertyConfiguration.setMailConfig(emailConfig);
		setMailConfiguration(emailConfig);
		configurationRepository.save(propertyConfiguration);
		logger.info("Mail configuration updated.", emailConfig.toString());
	}

	public String getUpdatedToken() {
		return slackConfiguration.getToken();
	}

	private void loadConfiguration() {
		PropertyConfiguration propertyConfiguration = getConfigurationFromDB();
		slackConfiguration = propertyConfiguration.getSlackConfig();
		mailConfiguration =  propertyConfiguration.getMailConfig();
	}

	private void propertyValidate() throws InvalidDataException {
		if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(userName) && 
				StringUtils.isNotBlank(message) && StringUtils.isNotBlank(channelmessage) &&
				StringUtils.isNotBlank(host) && StringUtils.isNotBlank(name) &&
				StringUtils.isNotBlank(password) && StringUtils.isNotBlank(from)) {
			return;
		}
		throw new InvalidDataException("Property file values not injected properly.");
	}

	private PropertyConfiguration getConfigurationFromDB() {
		return configurationRepository.findOne(new Sort(Sort.Direction.DESC,CONFIGURATION_FIELDS.ID));
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getChannelmessage() {
		return channelmessage;
	}

	public void setChannelmessage(String channelmessage) {
		this.channelmessage = channelmessage;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Slack getSlackConfiguration() {
		return slackConfiguration;
	}

	public void setSlackConfiguration(Slack slackConfiguration) {
		this.slackConfiguration = slackConfiguration;
	}

	public Email getMailConfiguration() {
		return mailConfiguration;
	}

	public void setMailConfiguration(Email mailConfiguration) {
		this.mailConfiguration = mailConfiguration;
	}

	public Map<String, String> getChannels() {
		return channels;
	}

	public void setChannels(Map<String, String> channels) {
		this.channels = channels;
	}

	public List<String> getValidEmailList() {
		return validEmailList;
	}

	public void setValidEmailList(List<String> validEmailList) {
		this.validEmailList = validEmailList;
	}

	public Map<String, Map<String, String>> getLocations() {
		return locations;
	}

	public void setLocations(Map<String, Map<String, String>> locations) {
		this.locations = locations;
	}

	public Map<String, String> getVisitorOrigins() {
		return visitorOrigins;
	}

	public void setVisitorOrigins(Map<String, String> visitorOrigins) {
		this.visitorOrigins = visitorOrigins;
	}

	public ConfigurationRepository getConfigurationRepository() {
		return configurationRepository;
	}

	public void setConfigurationRepository(ConfigurationRepository configurationRepository) {
		this.configurationRepository = configurationRepository;
	}

	public String getCategoryImagesPath() {
		return categoryImagesPath;
	}

	public void setCategoryImagesPath(String categoryImagesPath) {
		this.categoryImagesPath = categoryImagesPath;
	}

	public String getDepartmentImagesPath() {
		return departmentImagesPath;
	}

	public void setDepartmentImagesPath(String departmentImagesPath) {
		this.departmentImagesPath = departmentImagesPath;
	}

	public String getFileBaseDir() {
		return fileBaseDir;
	}

	public void setFileBaseDir(String fileBaseDir) {
		this.fileBaseDir = fileBaseDir;
	}

	public String getFileUploadsPath() {
		return fileUploadsPath;
	}

	public void setFileUploadsPath(String fileUploadsPath) {
		this.fileUploadsPath = fileUploadsPath;
	}

	public String getDefaultImagePath() {
		return defaultImagePath;
	}

	public void setDefaultImagePath(String defaultImagePath) {
		this.defaultImagePath = defaultImagePath;
	}

	public String getNdaFile() {
		return ndaFile;
	}

	public void setNdaFile(String ndaFile) {
		this.ndaFile = ndaFile;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	@Override
	public String toString() {
		return "ConfigurationService [configurationRepository=" + configurationRepository + ", host=" + host + ", port="
				+ port + ", name=" + name + ", password=" + password + ", from=" + from + ", userName=" + userName
				+ ", message=" + message + ", channelmessage=" + channelmessage + ", token=" + token + ", channels="
				+ channels + ", validEmailList=" + validEmailList + ", locations=" + locations + ", visitorOrigins="
				+ visitorOrigins + ", categoryImagesPath=" + categoryImagesPath + ", departmentImagesPath="
				+ departmentImagesPath + ", fileBaseDir=" + fileBaseDir + ", fileUploadsPath=" + fileUploadsPath
				+ ", defaultImagePath=" + defaultImagePath + ", ndaFile=" + ndaFile + ", baseUrl=" + baseUrl
				+ ", slackConfiguration=" + slackConfiguration + ", mailConfiguration=" + mailConfiguration + "]";
	}
}
