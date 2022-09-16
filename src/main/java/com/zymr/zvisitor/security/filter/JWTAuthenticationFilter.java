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
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zymr.zvisitor.resource.AppErrorController;
import org.apache.commons.io.IOUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.zymr.zvisitor.dbo.Users;
import com.zymr.zvisitor.util.Constants;
import com.zymr.zvisitor.util.JsonUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private AppErrorController log;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.setFilterProcessesUrl(Constants.LOGIN_URL);
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
			String json = IOUtils.toString(request.getInputStream());
			Users user = JsonUtils.fromJson(json, Users.class);
			if (Objects.isNull(user)) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, Constants.INVALID_PARAM);
			}
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), Collections.emptyList()));
		} catch (IOException  e) {
			logger.error("IOException while parsing JWT token. " ,e);
			throw new InternalAuthenticationServiceException(Constants.USER_AUTHENTICATON_ERROR_MSG);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,	HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {

		String token = Jwts.builder()
				.setSubject(((User) auth.getPrincipal()).getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + Constants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, Constants.SECRET.getBytes())
				.compact();
		response.addHeader(Constants.HEADER_STRING, token);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,	AuthenticationException failed) throws IOException, ServletException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, Constants.INVALID_LOGIN);
	}
}