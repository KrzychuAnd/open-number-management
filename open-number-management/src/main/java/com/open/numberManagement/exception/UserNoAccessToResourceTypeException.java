package com.open.numberManagement.exception;

import static com.open.numberManagement.util.Constants.ERR_RESOURCE_STATUS_NOT_FOUND;
import static com.open.numberManagement.util.Constants.ERR_USER_NO_ACCESS_TO_RESOURCE_TYPE;
import static com.open.numberManagement.util.Constants.ERR_USER_NO_ACCESS_TO_RESOURCE_TYPE_ID_MSG;
import static com.open.numberManagement.util.Constants.ERR_USER_NO_ACCESS_TO_RESOURCE_TYPE_NAME_MSG;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserNoAccessToResourceTypeException extends OpenNMAbstractRuntimeException {

	public UserNoAccessToResourceTypeException(Integer resTypeId) {
		super(String.format(ERR_USER_NO_ACCESS_TO_RESOURCE_TYPE_ID_MSG, resTypeId));
	}
	
	public UserNoAccessToResourceTypeException(String name) {
		super(String.format(ERR_USER_NO_ACCESS_TO_RESOURCE_TYPE_NAME_MSG, name));
	}
	
	@Override
	protected void setCommonFields() {
		this.setHttpStatus(HttpStatus.FORBIDDEN);
		this.setBusinessCode(ERR_USER_NO_ACCESS_TO_RESOURCE_TYPE);				
	}
}