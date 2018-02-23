package com.open.numberManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class ResourceInvalidAgainstBusinessRulesException extends RuntimeException {

	public ResourceInvalidAgainstBusinessRulesException(String name) {
		super("Resource name '" + name + "' is not valid against Business rules /Resource Type definition or Status lifecycle/.");
	}
	
}