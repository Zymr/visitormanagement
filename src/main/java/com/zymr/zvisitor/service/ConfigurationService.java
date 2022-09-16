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
import java.util.Arrays;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.dbo.config.Email;
import com.zymr.zvisitor.dbo.config.PropertyConfiguration;
import com.zymr.zvisitor.dbo.config.PropertyConfiguration.CONFIGURATION_FIELDS;
import com.zymr.zvisitor.dbo.config.Slack;
import com.zymr.zvisitor.exception.ZException;
import com.zymr.zvisitor.repository.ConfigurationRepository;
import com.zymr.zvisitor.service.config.AppProperties;
import com.zymr.zvisitor.service.config.SlackAccountConfig;
@Slf4j
@Service
public class ConfigurationService {

	@Autowired
	private ConfigurationRepository configurationRepository;
	
	@Autowired
	private AppProperties appProps;
	
	@Autowired
	private CryptoManager cryptoManager;

	private Slack slackConfiguration;
	
	private Email mailConfiguration;

	@PostConstruct
	public void init() throws IOException, ZException {
		if (configurationRepository.count() > 0) {
			loadConfiguration();
		} else {
			loadAndUpdateDbConfiguration();
		}
		log.info("Configuration Service", toString());
	}

	/** To save properties configuration into database. 
	 * 
	 * @throws IOException 
	 * @throws ZException */
	public void loadAndUpdateDbConfiguration() throws IOException, ZException {
		SlackAccountConfig slack = appProps.getSlack();
		this.slackConfiguration = new Slack(slack.getToken(), slack.getUserName(), slack.getMessage(), slack.getChannelmessage());
		
		MailProperties mailProps = appProps.getMailProperties();
		this.mailConfiguration = new Email(mailProps.getHost(), mailProps.getPort(), mailProps.getUsername(), cryptoManager.encryptString(mailProps.getPassword()), appProps.getMailPersonal());
		
		PropertyConfiguration propertyConfiguration = new PropertyConfiguration(slackConfiguration, mailConfiguration);
		configurationRepository.save(propertyConfiguration);
		
		this.mailConfiguration.setPassword(appProps.getMailProperties().getPassword());
		log.info("Property configuration saved.",  propertyConfiguration.toString());
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
		log.info("Slack configuration updated.", slackConfiguration.toString());
	}

	/**
	 * To update mail configuration.
	 * 
	 * @param Email
	 * @throws ZException 
	 */
	public void updateMailConfig(Email emailConfig) throws ZException {
		
		//For DB
		String passwordString = emailConfig.getPassword();
		emailConfig.setPassword(cryptoManager.encryptString(passwordString));
		
		PropertyConfiguration propertyConfiguration = getConfigurationFromDB();
		propertyConfiguration.setMailConfig(emailConfig);
		configurationRepository.save(propertyConfiguration);
		
		//For In Memory
		this.mailConfiguration = emailConfig;
		this.mailConfiguration.setPassword(passwordString);
		log.info("Mail configuration updated.", emailConfig.toString());
	}

	public String getUpdatedToken() {
		return slackConfiguration.getToken();
	}

	private void loadConfiguration() throws ZException {
		PropertyConfiguration propertyConfiguration = getConfigurationFromDB();
		slackConfiguration = propertyConfiguration.getSlackConfig();
		mailConfiguration =  propertyConfiguration.getMailConfig();
		String password = cryptoManager.decrypt(propertyConfiguration.getMailConfig().getPassword());
		mailConfiguration.setPassword(password);
	}

	private PropertyConfiguration getConfigurationFromDB() {
		return configurationRepository.findOne(Sort.by(Sort.Direction.DESC, CONFIGURATION_FIELDS.ID));
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
	public ConfigurationRepository getConfigurationRepository() {
		return configurationRepository;
	}
	public void setConfigurationRepository(ConfigurationRepository configurationRepository) {
		this.configurationRepository = configurationRepository;
	}

	@Override
	public String toString() {
		return "ConfigurationService [slackConfiguration=" + slackConfiguration + ", mailConfiguration="
				+ mailConfiguration + "]";
	}
	
}
