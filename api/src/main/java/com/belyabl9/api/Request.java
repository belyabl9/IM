package com.belyabl9.api;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
public class Request implements Serializable {
	private RequestType type;
	private Map<String, Object> content;
	
	public Request(RequestType type, Map<String, Object> content) {
		this.type = type;
		this.content = content;
	}
	
	public Request(RequestType type) {
		this.type = type;
	}
	
}
