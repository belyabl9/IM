package com.belyabl9.server.model.server;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity  
@Table(name="TextMessages")  
@PrimaryKeyJoinColumn(name="id")
@Getter
@Setter
public class TextMessage extends Message implements Serializable {

	public TextMessage() {	}
	
	@Column(name = "content", nullable = false)
	private String content;
	
	public TextMessage(User from, User to, Date date, String content) {
		super(from, to, date);
		this.content = content;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		TextMessage textMessage = (TextMessage) o;
		if (textMessage.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), textMessage.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}
}
