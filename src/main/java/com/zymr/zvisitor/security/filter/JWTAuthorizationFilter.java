/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.zymr.zvisitor.repository.UserRepository;
import com.zymr.zvisitor.util.Constants;
import com.zymr.zvisitor.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;

public class JWTAuthorizationFilter  extends BasicAuthenticationFilter {

	private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
	
	private UserRepository userRepository;

	public JWTAuthorizationFilter(AuthenticationManager authManager, UserRepository userRepository) {
		super(authManager);
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		try{
			String token = request.getHeader(Constants.HEADER_STRING);
			if (StringUtils.isBlank(token)) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, Constants.UNAUTHORIASED_MESSAGE); 	          
			} else {
				UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
				if (Objects.isNull(authentication)) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, Constants.UNAUTHORIASED_MESSAGE);
				}
				SecurityContextHolder.getContext().setAuthentication(authentication);
				chain.doFilter(request, response);
			}
		} catch (ExpiredJwtException e) {
			logger.error("ExpiredJwtException while parsing JWT token " ,e);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, Constants.UNAUTHORIASED_MESSAGE);
		} catch(Exception e) {
			logger.error("Exception while parsing JWT token " ,e);
			throw new InternalAuthenticationServiceException(Constants.USER_AUTHENTICATON_ERROR_MSG);
		}
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;
		String email = JwtUtil.parseJWTtoken(token);
		Long count = userRepository.countByEmail(email);
		if (Objects.nonNull(count) && count > 0) {
			usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
		}
		return usernamePasswordAuthenticationToken;
	}
}