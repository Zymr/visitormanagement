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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

	@Autowired
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public UserDetailsServiceImpl() {
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			return new User("admin",passwordEncoder.encode("123456"), emptyList());
		} catch (Exception e) {
			logger.error("Error while decrpt password of user. Email {}" , email , e);
		}
		return null;
	}
}
