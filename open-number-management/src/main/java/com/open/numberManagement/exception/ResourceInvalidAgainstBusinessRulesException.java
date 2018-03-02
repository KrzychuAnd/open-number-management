package com.open.numberManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class ResourceInvalidAgainstBusinessRulesException extends RuntimeException {
	
	public ResourceInvalidAgainstBusinessRulesException(Integer ErrorCode, String ErrorMessage) {
		super(ErrorCode + " - " + ErrorMessage);
	}
	
}