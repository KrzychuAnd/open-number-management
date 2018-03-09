package com.open.numberManagement.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public abstract class OpenNMAbstractRuntimeException extends RuntimeException {

	private HttpStatus httpStatus;
	private Integer businessCode;
	
	public OpenNMAbstractRuntimeException(String message) {
		super(message);
		setCommonFields();
	}
	
	protected abstract void setCommonFields();
}
