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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SyncJob {
	protected static final Logger logger = LoggerFactory.getLogger(SyncJob.class);

	@Autowired
	protected EmployeeService employeeService;

	/** This method is used to sync employees with slack. */

	@Scheduled(cron = "${job.cronexp}")
	public void syncEmployees() {
		try {
			employeeService.upsertEmployeeFromSlack();
			logger.info("Employee sync job is completed");
		} catch(Exception e) {
			logger.error("Exception in employee sync job",e);
		}
	}
}
