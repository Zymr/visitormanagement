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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.dbo.Employee;
import com.zymr.zvisitor.dbo.SlackChannel;
import com.zymr.zvisitor.dbo.Visitor;
import com.zymr.zvisitor.dto.EmailDTO;
import com.zymr.zvisitor.util.Constants;
import com.zymr.zvisitor.util.Util;
import com.zymr.zvisitor.util.enums.NotificationKey;

@Service
public class NotificationService {
	protected static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private SlackService slackService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired                 
	private EmailService emailService; 

	@Autowired                 
	private LocationService locationService; 

	@Autowired
	private ImageService imageService;

	private static String emailTemplate;
	private static String ndaEmailTemplate;

	@PostConstruct
	public void init() throws IOException {
		logger.info("Notification Service {} ", toString());
		emailTemplate = IOUtils.toString(EmailService.class.getClassLoader()
				.getResourceAsStream(Constants.EMAIL_TEMPLATE), Charset.defaultCharset());
		ndaEmailTemplate = IOUtils.toString(EmailService.class.getClassLoader()
				.getResourceAsStream(Constants.NDA_EMAIL_TEMPLATE), Charset.defaultCharset());
	}

	/**
	 * This method is used to send slack and email notification.  
	 * 
	 * @param empSlackId slackId of employee
	 * @param channelId channelId of Channel
	 * @param Visitor visitor information
	 * @param ndaFilePath path of nda file
	 * 
	 * @return boolean
	 * @throws IOException 
	 * @throws AddressException 
	 */  
	public boolean notify(String empSlackId, String channelId, Visitor visitor, String ndaFilePath) throws AddressException, IOException  {
		logger.info("Started sending slack/email notification.");
		Employee employee  = null;
		SlackChannel channel = null;
		String locationName = locationService.getLocationName(visitor.getLocation()).getLocationName();
		if (StringUtils.isNotBlank(empSlackId) && StringUtils.isBlank(channelId)) {
			employee = employeeService.getBySlackId(empSlackId);                          
		} else {
			channel = channelService.findByChannelId(channelId);
		}
		if (Objects.nonNull(visitor)) {
				notifyOnSlack(employee, visitor, channel, locationName);
				notifyOnEmail(employee, visitor, channel, ndaFilePath, locationName);
		}
		return true;
	}

	/**
	 * This method is used to send email notification.  
	 * 
	 * @param mail list of map containing mail headers details. eg. "from"
	 * 
	 * @return nothing
	 * @throws IOException 
	 * @throws AddressException 
	 */  
	private void notifyOnEmail(Employee employee, Visitor visitor, SlackChannel channel, String ndaFilePath, String locationName) throws AddressException, IOException {
		EmailDTO emailDTO = createMail(employee, visitor,channel, locationName);
		List<EmailDTO> emailList = new ArrayList<EmailDTO>();
		emailList.add(emailDTO);
		if (Objects.nonNull(visitor)) {
			if (StringUtils.isNotBlank(visitor.getEmail()) && StringUtils.isNotBlank(ndaFilePath)) {
				emailList.add(createNDAMail(visitor, ndaFilePath));
			}
		}
		emailList.forEach(mail -> emailService.sendEmail(mail));
	}

	/**
	 * This method is used to send slack notification.  
	 * 
	 * @return boolean slack response
	 * @throws IOException 
	 */  
	private void notifyOnSlack(Employee employee, Visitor visitor, SlackChannel channel, String locationName) throws IOException {
		Map<String,ContentBody> attachment = createAttachment(imageService.getBaseDirPathAsStr(), visitor.getVisitorPic(), imageService.getDefaultImageFile());
		Map<String, String> param = null;
		if (Objects.nonNull(employee)) {
			String	slackMessage = createMessage(configurationService.getSlackConfiguration().getMessage(),
					visitor, employee, locationName);

			param = buildSlackRequestParam(employee.getSlackId(), slackMessage, configurationService.getSlackConfiguration().getToken());			
		}
		else  {	
			String	channelMessage = messageForChannel(configurationService.getSlackConfiguration().
					getChannelMessage(), visitor, channel, locationName);

			param = buildSlackRequestParam(channel.getChannelId(), channelMessage, configurationService.getSlackConfiguration().getToken());	
		}
		slackService.sendMessage(param, attachment);
	}

	/**
	 * This method is used to create email headers.  
	 * 
	 * @return map containing header values.
	 * @throws AddressException 
	 * @throws IOException 
	 */  
	private EmailDTO createMail(Employee employee, Visitor visitor, SlackChannel channel, String locationName) throws AddressException, IOException {
		String message;
		File file = null;
		String to = null;
		if (StringUtils.isNotBlank(visitor.getVisitorPic())) {
			file = new File(Util.getImageFullPath(imageService.getBaseDirPath().toString(), visitor.getVisitorPic()));
		} else {
			file = imageService.getDefaultImageFile();
		}
		String from = new InternetAddress(configurationService.getMailConfiguration().getUsername()).toString();
		if (Objects.nonNull(employee)) {
			message = createMessage(emailTemplate, visitor, employee, locationName);
			to = new InternetAddress(employee.getEmail()).toString();
		} else  {	
			message = messageForChannel(emailTemplate, visitor, channel, locationName);
			to = new InternetAddress(channel.getEmail()).toString();
		}
		return new EmailDTO(Constants.NOTIFICATION_MAIL_SUBJECT, message, from, to, file, Constants.NOTIFICATION_IMAGE_NAME);
	}

	/**
	 * This method is used to create nda email headers.  
	 * 
	 * @return mail list of map containing nda mail headers details. eg. "from"
	 * @throws AddressException 
	 */  
	private EmailDTO createNDAMail(Visitor visitor, String ndaFilePath) throws AddressException {
		String message = ndaEmailTemplate;
		message = message.replace(Constants.VISITOR_NAME, visitor.getName());
		String from = new InternetAddress(configurationService.getMailConfiguration().getUsername()).toString();
		String to = new InternetAddress(visitor.getEmail()).toString();
		File attachmentFile = null;
		if (StringUtils.isNotBlank(ndaFilePath)) {
			attachmentFile = new File(ndaFilePath);
		} 	
		return new EmailDTO(Constants.NDA_MAIL_SUBJECT, message, from, to, attachmentFile, Constants.NDA_ATTACHMENT_NAME);
	}


	/** This method is used to create message. It will replace place holders with real values.
	 * 
	 * @param message
	 * @param visitor
	 * @param employee
	 * @param location
	 * @return String
	 */
	private String createMessage(String message, Visitor visitor, Employee employee, String location) {
		return message.replace(Constants.EMPLOYEE_FULLNAME, employee.getName())
				.replace(Constants.VISITOR_NAME, visitor.getName())
				.replace(Constants.VISITOR_FROM, visitor.getCategoryName())  
				.replace(Constants.EMPLOYEE_NAME, employee.getFullName()) 
				.replace(Constants.VISITOR_lOCATION, location)
				.replace(Constants.VISITOR_PURPOSE, StringUtils.isEmpty(visitor.getPurpose()) ? Constants.VISITOR_DEFAULT_PURPOSE : visitor.getPurpose().trim());
	}

	/**This method is used to create channel message. It will replace place holders with real values.
	 * 
	 * @param message
	 * @param visitor
	 * @param channel
	 * @param location
	 * @return String
	 */
	private String messageForChannel(String message, Visitor visitor, SlackChannel channel, String location) {
		return message.replace(Constants.EMPLOYEE_FULLNAME,Constants.SLACK_CHANNEL_NOTIFICATION_PARAMNAME)
				.replace(Constants.VISITOR_NAME, visitor.getName())
				.replace(Constants.EMPLOYEE_NAME, channel.getName())
				.replace(Constants.VISITOR_lOCATION, location)
				.replace(Constants.VISITOR_FROM, visitor.getCategoryName())
				.replace(Constants.VISITOR_PURPOSE, StringUtils.isBlank(visitor.getPurpose()) ? Constants.VISITOR_DEFAULT_PURPOSE : visitor.getPurpose().trim());
	}

	/**This method is used to create post request body to send slack notification.
	 * 
	 * @param empId
	 * @param message
	 * @param token
	 * @return 
	 */
	private Map<String, String> buildSlackRequestParam(String empId, String message, String token) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put(NotificationKey.token.name(), token);
		parameters.put(NotificationKey.initial_comment.name(), message);
		parameters.put(NotificationKey.channels.name(), empId);
		parameters.put(NotificationKey.title.name(), Constants.NOTIFICATION_IMAGE_NAME);
		return parameters;
	}

	/**
	 * @param dirPath
	 * @param imageDbpath
	 * @param defaultImageFile
	 * @return
	 * @throws IOException
	 */
	public  Map<String, ContentBody> createAttachment(String dirPath, String imageDbpath, File defaultImageFile) throws IOException {
		Map<String, ContentBody> parameters = new HashMap<>();
		if (StringUtils.isNotBlank(imageDbpath)) {
			parameters.put(NotificationKey.file.name(), new FileBody(new File(Util.getImageFullPath(dirPath, imageDbpath))));
		} else {
			parameters.put(NotificationKey.file.name(), new FileBody(defaultImageFile));
		} 
		return parameters;
	}

	public static void setEmailTemplate(String emailTemplate) {
		NotificationService.emailTemplate = emailTemplate;
	}

	public static void setNDAEmailTemplate(String ndaEmailTemplate) {
		NotificationService.ndaEmailTemplate = ndaEmailTemplate;
	}

	@Override
	public String toString() {
		return "NotificationService [employeeService=" + employeeService + ", channelService=" + channelService
				+ ", slackService=" + slackService + ", configurationService=" + configurationService
				+ ", emailService=" + emailService + ", locationService=" + locationService + ", imageService="
				+ imageService + "]";
	}
}
