package com.open.numberManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserNoAccessToResourceTypeException extends RuntimeException {

	public UserNoAccessToResourceTypeException(Integer resTypeId) {
		super("You do not have access to Resource Type with id '" + resTypeId + "'.");
	}
	
	public UserNoAccessToResourceTypeException(String name) {
		super("You do not have access to Resource Type with name '" + name + "'.");
	}
}