package com.open.numberManagement.exception;

import static com.open.numberManagement.util.Constants.ERR_USER_NO_ACCESS;
import static com.open.numberManagement.util.Constants.ERR_USER_NO_ACCESS_ID_MSG;
import static com.open.numberManagement.util.Constants.ERR_USER_NO_ACCESS_LOGIN_MSG;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NoAccessToUserException extends OpenNMAbstractRuntimeException {

	public NoAccessToUserException(Integer userId) {
		super(String.format(ERR_USER_NO_ACCESS_ID_MSG, userId));
	}
	
	public NoAccessToUserException(String login) {
		super(String.format(ERR_USER_NO_ACCESS_LOGIN_MSG, login));
	}

	@Override
	protected void setCommonFields() {
		this.setHttpStatus(HttpStatus.FORBIDDEN);
		this.setBusinessCode(ERR_USER_NO_ACCESS);
	}
}