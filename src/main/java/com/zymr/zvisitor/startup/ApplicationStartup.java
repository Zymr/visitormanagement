/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.zymr.zvisitor.service.ChannelService;
import com.zymr.zvisitor.service.EmployeeService;
import com.zymr.zvisitor.service.LocationService;
import com.zymr.zvisitor.service.VisitorService;

/**
 * @author chirag.b
 */
@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
	protected static final Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);

	@Autowired	
	private EmployeeService employeeService;

	@Autowired	
	private ChannelService channelService;

	@Autowired	
	private VisitorService visitorService;
	
	@Autowired
	private LocationService locationService;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		try {
			locationService.syncLocation();
			channelService.syncChannelsFromSlack();
			employeeService.syncEmployeeFromSlack();
			visitorService.syncVisitorOrigin();
		} catch(Exception e) {
			logger.error("Exception while syncing on startup {}.", e);
			throw new RuntimeException(e);
		}
	}
}
