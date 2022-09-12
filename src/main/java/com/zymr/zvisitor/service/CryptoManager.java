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

import static org.apache.commons.codec.binary.Hex.decodeHex;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.exception.ZException;
import com.zymr.zvisitor.service.config.AppProperties;
import com.zymr.zvisitor.util.Constants;

@Service
public class CryptoManager {

	private static final Logger logger = LoggerFactory.getLogger(CryptoManager.class);

	@Autowired
	private AppProperties appProperties;

	private SecretKeySpec key = null;

	@PostConstruct
	public void init() throws ZException {
		key = loadKey();
	}

	/*
	 * Encrypt Password
	 */
	public String encryptString(String text) throws ZException {
		return text;
//		if(StringUtils.isBlank(text))
//			return null;
//		try {
//			Cipher cipher = Cipher.getInstance(Constants.ENCRYPT_TRANSFORMATION);
//			byte[] iv = new byte[cipher.getBlockSize()];
//			IvParameterSpec ivParams = new IvParameterSpec(iv);
//			cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
//			byte[] cipherText = cipher.doFinal(text.getBytes(Constants.CHARSET));
//			return Base64.encodeBase64String(cipherText);
//		} catch(Exception e) {
//			logger.error("error while encrypting string, returning null",e);
//			throw new ZException(e);
//		}
	}

	/*
	 * Decrypt Password
	 */
	public  String decrypt(String encryptedText) throws ZException {
		return encryptedText;
//		if (StringUtils.isBlank(encryptedText)) {
//			return null;
//		}
//		try {
//			Cipher cipher = Cipher.getInstance(Constants.ENCRYPT_TRANSFORMATION);
//			byte[] iv = new byte[cipher.getBlockSize()];
//			IvParameterSpec ivParams = new IvParameterSpec(iv);
//			cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
//			byte[] cipherText = Base64.decodeBase64(encryptedText.trim());
//			return new String(cipher.doFinal(cipherText), Constants.CHARSET);
//		} catch(Exception e) {
//			logger.error("error while decrypting string, returning null",e);
//			throw new ZException(e);
//		}
	}

	private SecretKeySpec loadKey() throws ZException
	{
		char[] hex = appProperties.getSecretKey().toCharArray();
		byte[] encoded;
		try {
			encoded = decodeHex(hex);
		} catch(DecoderException e) {
			logger.error("DecoderException in loadKey method " ,e);
			throw new ZException(e);
		}
		return new SecretKeySpec(encoded, Constants.ENCRYPT_ALGORITHM_AES);
	}
}
