package com.belyabl9.api;

import java.io.Serializable;

public class Response implements Serializable {
	public static int ERROR = 0;
	public static int SUCCESS = 1;
	
	private int status;
	private Object content;
	
	
	public Response(int status, Object content) {
		this.status = status;
		this.content = content;
	}
	
	public boolean isSuccessful() {
		return status == SUCCESS;
	}
	
	public Object getContent() {
		return content;
	}
}
