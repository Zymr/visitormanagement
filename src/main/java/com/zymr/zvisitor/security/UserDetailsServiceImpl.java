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

import com.zymr.zvisitor.repository.UserRepository;
import com.zymr.zvisitor.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService{

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
			log.error("Error while decrypt password of user. Email {}" , email , e);
		}
		return null;
	}
}
