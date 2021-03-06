package com.open.numberManagement.exception;

import static com.open.numberManagement.util.Constants.ERR_USER_NO_ACCESS_TO_RESOURCE;
import static com.open.numberManagement.util.Constants.ERR_USER_NO_ACCESS_TO_RESOURCE_ID_MSG;
import static com.open.numberManagement.util.Constants.ERR_USER_NO_ACCESS_TO_RESOURCE_NAME_MSG;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserNoAccessToResourceException extends OpenNMAbstractRuntimeException {

	public UserNoAccessToResourceException(Integer resId) {
		super(String.format(ERR_USER_NO_ACCESS_TO_RESOURCE_ID_MSG, resId));
	}
	
	public UserNoAccessToResourceException(String name) {
		super(String.format(ERR_USER_NO_ACCESS_TO_RESOURCE_NAME_MSG, name));
	}

	@Override
	protected void setCommonFields() {
		this.setHttpStatus(HttpStatus.FORBIDDEN);
		this.setBusinessCode(ERR_USER_NO_ACCESS_TO_RESOURCE);
	}
}