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

import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.dto.EmailConfigurationDTO;
import com.zymr.zvisitor.dto.EmailDTO;
import com.zymr.zvisitor.util.Constants;

@Service
public class EmailService {
	protected static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private ConfigurationService configurationService;

	private JavaMailSenderImpl  javaMailSenderImpl = new JavaMailSenderImpl();

	private static final String SMTP_PROTOCOL = "smtp";

	@PostConstruct
	public void init() {
		updateMailSenderDefaultProps();
		updateMailSenderConfig();
		logger.info("Email Service {} ", toString());
	}

	/**
	 * To send email.
	 * 
	 * @param mailParam its map containing mail headers details. i.e. "from"
	 */  
	public void sendEmail(EmailDTO emailDTO) {
		try { 
			if (Objects.nonNull(emailDTO)) {
				MimeMessage  mail = javaMailSenderImpl.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mail, true);
				helper.setSubject(emailDTO.getSubject());
				helper.setText(emailDTO.getMessage(), true);
				helper.setFrom(emailDTO.getFrom(), configurationService.getMailConfiguration().getFrom());
				helper.setTo(emailDTO.getTo());
				if (Objects.nonNull(emailDTO.getAttachment())) {
					helper.addAttachment(emailDTO.getAttachmentName(), emailDTO.getAttachment());
				}
				javaMailSenderImpl.send(mail);
			}
		} catch (MessagingException | MailException | UnsupportedEncodingException e) {
			logger.error("Exception while sending mail notification.", e);
		}
	}

	/** Test smtp authentication configuration.
	 * 
	 * @param smtpConfiguration
	 * @throws MessagingException
	 * @throws ConfigurationException
	 */
	public void authenticateAndLoadConfiguration(EmailConfigurationDTO smtpConfiguration) throws MessagingException, ConfigurationException {
		Transport transport = getSession().getTransport(SMTP_PROTOCOL);
		try {
			transport.connect(smtpConfiguration.getHost(), smtpConfiguration.getPort(),	smtpConfiguration.getUserName(), 
					smtpConfiguration.getPassword());
		} finally {
			transport.close();
		}
		updateMailSenderConfig();
	}

	private Session getSession() {
		return Session.getInstance(javaMailSenderImpl.getJavaMailProperties());
	}

	private void updateMailSenderConfig() {
		javaMailSenderImpl.setHost(configurationService.getMailConfiguration().getHost());
		javaMailSenderImpl.setPort(configurationService.getMailConfiguration().getPort());
		javaMailSenderImpl.setUsername(configurationService.getMailConfiguration().getUsername());
		javaMailSenderImpl.setPassword(configurationService.getMailConfiguration().getPassword());
	}

	private void updateMailSenderDefaultProps() {
		Properties properties = javaMailSenderImpl.getJavaMailProperties();
		properties.put(Constants.MAIL_TRANSPORT_PROTOCOL, SMTP_PROTOCOL);
		properties.put(Constants.MAIL_SMTP_AUTH, Boolean.TRUE);
		properties.put(Constants.MAIL_SMTP_ENABLE, Boolean.TRUE);
		properties.put(Constants.MAIL_DEBUG, Boolean.TRUE);
		javaMailSenderImpl.setJavaMailProperties(properties);
	}

	@Override
	public String toString() {
		return "EmailService [configurationService=" + configurationService + ", javaMailSenderImpl="
				+ javaMailSenderImpl + "]";
	}
}
