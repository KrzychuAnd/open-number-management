package com.open.numberManagement.exception;

import static com.open.numberManagement.util.Constants.ERR_USER_NOT_FOUND;
import static com.open.numberManagement.util.Constants.ERR_USER_NOT_FOUND_ID_MSG;
import static com.open.numberManagement.util.Constants.ERR_USER_NOT_FOUND_LOGIN_MSG;
import static com.open.numberManagement.util.Constants.ERR_USER_NO_ACCESS_TO_RESOURCE_TYPE;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends OpenNMAbstractRuntimeException {

	public UserNotFoundException(Integer userId) {
		super(String.format(ERR_USER_NOT_FOUND_ID_MSG, userId));
	}
	
	public UserNotFoundException(String login) {
		super(String.format(ERR_USER_NOT_FOUND_LOGIN_MSG, login));
	}

	@Override
	protected void setCommonFields() {
		this.setHttpStatus(HttpStatus.NOT_FOUND);
		this.setBusinessCode(ERR_USER_NOT_FOUND);
	}
	
}