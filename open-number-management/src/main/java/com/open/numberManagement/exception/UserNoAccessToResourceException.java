package com.open.numberManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserNoAccessToResourceException extends RuntimeException {

	public UserNoAccessToResourceException(Integer resId) {
		super("You do not have access to resource with id '" + resId + "'.");
	}
	
	public UserNoAccessToResourceException(String name) {
		super("You do not have access to Resource with name '" + name + "'.");
	}
}