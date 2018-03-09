package com.open.numberManagement.exception;

import static com.open.numberManagement.util.Constants.ERR_RESOURCE_GENERATE_MAX_NUMBER_EXCEEDED;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_GENERATE_MAX_NUMBER_EXCEEDED_MSG;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class ResourceGenerateLimitNumberException extends OpenNMAbstractRuntimeException {

	public ResourceGenerateLimitNumberException() {
		super(ERR_RESOURCE_GENERATE_MAX_NUMBER_EXCEEDED_MSG);
	}

	@Override
	protected void setCommonFields() {
		this.setHttpStatus(HttpStatus.PRECONDITION_FAILED);
		this.setBusinessCode(ERR_RESOURCE_GENERATE_MAX_NUMBER_EXCEEDED);
	}
	
}