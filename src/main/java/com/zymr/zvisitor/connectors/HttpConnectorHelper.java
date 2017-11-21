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

import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.BasicResponseHandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.zymr.zvisitor.util.JsonUtils;

public class HttpConnectorHelper {
	
	private HttpConnectorHelper() {
		
	}
	
	/** 
	 * 
	 * @param map
	 * 
	 * @return HttpEntity
	 */
	public static HttpEntity buildEntityWithBodyParam(Map<String, String> map) {
		if (MapUtils.isEmpty(map)) {
			return null;
		}
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		map.forEach((k, v) -> builder.addTextBody(k, v));
		return builder.build();
	}

	/**
	 * @param buildMap
	 * @param partParam
	 * 
	 * @return HttpEntity
	 */
	public static HttpEntity buildMultiPartEntity(Map<String, String> buildMap, Map<String, ContentBody> partParam) {
		if (MapUtils.isEmpty(buildMap)) {
			return null;
		}
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		buildMap.forEach((k, v) -> builder.addTextBody(k, v));
		if (MapUtils.isNotEmpty(partParam)) {
			partParam.forEach((k, v) -> builder.addPart(k, v));	
		}
		return builder.build();
	}

	/**
	 * @param httpResponse
	 * @param classOfT
	 * 
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> T fromResponseToObj(HttpResponse httpResponse, Class<T> classOfT) throws  JsonMappingException, IOException{
		return JsonUtils.fromJson(new BasicResponseHandler().handleResponse(httpResponse), classOfT);
	}


	public static String fromResponseToString(HttpResponse httpResponse) throws HttpResponseException, IOException{
		return new BasicResponseHandler().handleResponse(httpResponse);
	}
}
