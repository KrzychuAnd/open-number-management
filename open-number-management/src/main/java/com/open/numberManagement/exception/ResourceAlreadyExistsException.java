package com.open.numberManagement.exception;

import static com.open.numberManagement.util.Constants.ERR_RESOURCE_ALREADY_EXISTS;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_ALREADY_EXISTS_NAME_MSG;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class ResourceAlreadyExistsException extends OpenNMAbstractRuntimeException {

	public ResourceAlreadyExistsException(String name) {
		super(String.format(ERR_RESOURCE_ALREADY_EXISTS_NAME_MSG, name));
	}

	@Override
	protected void setCommonFields() {
		this.setHttpStatus(HttpStatus.PRECONDITION_FAILED);
		this.setBusinessCode(ERR_RESOURCE_ALREADY_EXISTS);
	}	
}