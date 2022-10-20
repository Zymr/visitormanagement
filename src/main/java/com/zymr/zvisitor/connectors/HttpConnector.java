/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.connectors;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zymr.zvisitor.service.SlackService;

/** A http connector class to execute http requests. */

@Service
public class HttpConnector {

	private static final Logger logger = LoggerFactory.getLogger(HttpConnector.class);

	/**
	 * Method to send post request.
	 * 
	 * @param HttpEntity request body
	 * @param url the URL
	 * 
	 * @return CloseableHttpResponse
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public CloseableHttpResponse postRequest(HttpEntity httpEntity, String url) throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(httpEntity);
		return client.execute(httpPost);
	}

	public CloseableHttpResponse postRequest(final HttpEntity httpEntity, final Map<String, String> headers, final String url)
			throws ClientProtocolException, IOException {
		logger.info(headers.toString());

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(httpEntity);
		if (Objects.nonNull(headers)) {
			headers.forEach((k, v) -> httpPost.addHeader(k, v));
		}
		return client.execute(httpPost);
	}
}
