package com.belyabl9.api;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DomainSuperClass implements Serializable {
	protected long id;
}
