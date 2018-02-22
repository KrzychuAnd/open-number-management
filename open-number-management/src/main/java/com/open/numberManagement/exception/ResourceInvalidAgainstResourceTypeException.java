package com.open.numberManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ResourceInvalidAgainstResourceTypeException extends RuntimeException {

	public ResourceInvalidAgainstResourceTypeException(String name) {
		super("Resource name '" + name + "' is not valid against Resource Type definition.");
	}
	
}