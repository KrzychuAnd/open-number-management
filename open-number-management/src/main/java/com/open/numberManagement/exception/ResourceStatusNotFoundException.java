package com.open.numberManagement.exception;

import static com.open.numberManagement.util.Constants.ERR_RESOURCE_STATUS_NOT_FOUND;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_STATUS_NOT_FOUND_MSG;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceStatusNotFoundException extends RuntimeException {

	public ResourceStatusNotFoundException(Integer statusId) {
		super(ERR_RESOURCE_STATUS_NOT_FOUND + " - " + String.format(ERR_RESOURCE_STATUS_NOT_FOUND_MSG, statusId));
	}
	
}