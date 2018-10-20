package com.belyabl9.server.model.server;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity  
@Table(name="MediaMessages")  
@PrimaryKeyJoinColumn(name="id")  
@Getter
@Setter
public class MediaMessage extends Message {

	@Column(name = "path", nullable = false)
	private String path;
	
	@Column(name = "subject", nullable = false)
	private String subject;

	@Lob
	@Column(name = "data", nullable = false)
	private byte[] blob;
	
	public MediaMessage() { }
	
	public MediaMessage(User from, User to, Date date, String subject, String filename, byte[] blob) {
		super(from, to, date);
		this.path = filename;
		this.subject = subject;
		this.blob = blob;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MediaMessage mediaMessage = (MediaMessage) o;
		if (mediaMessage.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), mediaMessage.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

}
