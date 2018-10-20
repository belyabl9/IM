package com.belyabl9.api;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MediaMessage extends Message {
	private String path;
	private String subject;
	private byte[] blob;
	
	public MediaMessage(long id, User from, User to, Date date, String subject, String filename, byte[] blob) {
		super(id, from, to, date);
		this.subject = subject;
		this.path = filename;
		this.blob = blob;
	}

	public MediaMessage(User from, User to, Date date, String subject, String filename, byte[] blob) {
		super(from, to, date);
		this.path = filename;
		this.subject = subject;
		this.blob = blob;
	}
}