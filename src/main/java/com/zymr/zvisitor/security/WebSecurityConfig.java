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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

import com.zymr.zvisitor.repository.UserRepository;
import com.zymr.zvisitor.security.filter.CORSFilter;
import com.zymr.zvisitor.security.filter.JWTAuthenticationFilter;
import com.zymr.zvisitor.security.filter.JWTAuthorizationFilter;
import com.zymr.zvisitor.util.Constants;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserRepository userRepository;

	public WebSecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
		.antMatchers(HttpMethod.POST, Constants.LOGIN_URL).permitAll()
		.and()
		.addFilter(new JWTAuthenticationFilter(authenticationManager()))
		.addFilter(new JWTAuthorizationFilter(authenticationManager(), userRepository))
		.addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class)
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(Constants.BASE_URL+ Constants.FORWARD_SLASH_AND_ANY,
				Constants.IMAGE_URL+Constants.FORWARD_SLASH_AND_ANY,
				Constants.FILE_URL+Constants.FORWARD_SLASH_AND_ANY,
				Constants.SWAGGER_URL+Constants.FORWARD_SLASH_AND_ANY,
				Constants.SWAGGER_VERSION_URL,
				Constants.SWAGGER_UI,
				Constants.SWAGGER_RESOURCES, 
				Constants.SWAGGER_SECURITY, Constants.SWAGGER_SECURITY_URL);
		web.ignoring().regexMatchers("NDA\\**");
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
}
