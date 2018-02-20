package com.open.numberManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(Integer resId) {
		super("Could not find resource '" + resId + "'.");
	}
	
	public ResourceNotFoundException(String name) {
		super("Could not find resource '" + name + "'.");
	}
	
	public ResourceNotFoundException(Integer resTypeId, boolean isResType) {
		super("Could not find resource with resource type Id '" + resTypeId + "'.");
	}
	
	public ResourceNotFoundException(String resType, boolean isResType) {
		super("Could not find resource with resource type '" + resType + "'.");
	}	
}