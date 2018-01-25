/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.security;

import static java.util.Collections.emptyList;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.dbo.Users;
import com.zymr.zvisitor.exception.ZException;
import com.zymr.zvisitor.repository.UserRepository;
import com.zymr.zvisitor.service.ConfigurationService;
import com.zymr.zvisitor.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;

	public UserDetailsServiceImpl() {
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Users user = userRepository.findByEmail(email);
		if (Objects.isNull(user)) {
			throw new UsernameNotFoundException(email);
		}
		try {
			return new User(user.getEmail(), userService.decryptPassword(user.getPassword()), emptyList());
		} catch (ZException e) {
			logger.error("Error while decrpt password of user. Email {}" , email , e);
		}
		return null;
	}
}
