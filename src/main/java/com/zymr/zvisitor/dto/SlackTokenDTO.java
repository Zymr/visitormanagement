package com.zymr.zvisitor.dto;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

@JsonInclude(Include.NON_EMPTY)
public class SlackTokenDTO {
	
	@JsonProperty("token")
	@NotBlank
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "SlackTokenDTO [token=" + token + "]";
	}
}
