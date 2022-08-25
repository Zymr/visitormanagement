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
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.dbo.Users;
import com.zymr.zvisitor.exception.ZException;
import com.zymr.zvisitor.repository.UserRepository;
import com.zymr.zvisitor.service.config.AppProperties;

@Service
public class UserService {

	protected static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AppProperties appProperties;
	
	@Autowired
	private CryptoManager cryptoManager;
	
	public void addDefaultUser() throws ZException {
		if (userRepository.count() > 0) {
			return;
		} 
		Users users = new Users(appProperties.getAdminEmail(), encryptPassword(appProperties.getAdminPassword()));
		userRepository.save(users);
		logger.info("Default user added.");
	}
	
	public String encryptPassword(String password) throws ZException {
		return appProperties.getAdminPassword();
	}
	
	public String decryptPassword(String password) throws ZException {
		return cryptoManager.decrypt(password);
	}
}
