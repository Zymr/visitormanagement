package com.zymr.zvisitor.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author Chirag Bhimani
 *
 */
public class JwtUtil {

	public static String generateJWTtoken(Authentication auth) {
		return Jwts.builder()
				.setSubject(((User) auth.getPrincipal()).getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)))
				.signWith(SignatureAlgorithm.HS512, Constants.SECRET.getBytes())
				.compact();
	}
	
	public static String parseJWTtoken(String token) {
		return Jwts.parser()
				.setSigningKey(Constants.SECRET.getBytes())
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
}