package com.open.numberManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class ResourceInvalidAgainstBusinessRulesException extends OpenNMAbstractRuntimeException {
	
	public ResourceInvalidAgainstBusinessRulesException(Integer ErrorCode, String ErrorMessage) {
		super(ErrorMessage);
		this.setBusinessCode(ErrorCode);
	}

	@Override
	protected void setCommonFields() {
		this.setHttpStatus(HttpStatus.PRECONDITION_FAILED);
	}
	
}