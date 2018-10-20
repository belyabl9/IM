package com.belyabl9.server.model.server;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
@Getter
@Setter
public class DomainSuperClass implements Serializable {
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	protected Long id;
}
