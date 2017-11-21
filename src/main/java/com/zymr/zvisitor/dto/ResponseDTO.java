/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.dto;

import java.util.HashMap;
import java.util.Map;

public class ResponseDTO {
	private Map<String, Object> responseMap;

	public ResponseDTO(String key, Object object) {
		this.responseMap = new HashMap<>();
		this.responseMap.put(key, object);
	}
	
	public Map<String, Object> getResponse() {
		return responseMap;
	}

	public void setResponse(Map<String, Object> responseMap) {
		this.responseMap = responseMap;
	}
	
	@Override
	public String toString() {
		return "ResponseDTO [responseMap=" + responseMap + "]";
	}
}
