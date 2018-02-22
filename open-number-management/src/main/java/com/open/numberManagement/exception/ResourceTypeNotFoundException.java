package com.open.numberManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceTypeNotFoundException extends RuntimeException {

	public ResourceTypeNotFoundException(Integer resTypeId) {
		super("Could not find Resource Type with id: '" + resTypeId + "'.");
	}

}