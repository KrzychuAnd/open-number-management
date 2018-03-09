package com.open.numberManagement.exception;

import static com.open.numberManagement.util.Constants.ERR_RESOURCE_STATUS_NOT_FOUND;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_STATUS_NOT_FOUND_MSG;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceStatusNotFoundException extends OpenNMAbstractRuntimeException {

	public ResourceStatusNotFoundException(Integer statusId) {
		super(String.format(ERR_RESOURCE_STATUS_NOT_FOUND_MSG, statusId));
	}

	@Override
	protected void setCommonFields() {
		this.setHttpStatus(HttpStatus.NOT_FOUND);
		this.setBusinessCode(ERR_RESOURCE_STATUS_NOT_FOUND);	
	}
	
}