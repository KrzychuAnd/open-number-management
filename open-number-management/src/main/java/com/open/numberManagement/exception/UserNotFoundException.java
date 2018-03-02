package com.open.numberManagement.exception;

import static com.open.numberManagement.util.Constants.ERR_USER_NOT_FOUND;
import static com.open.numberManagement.util.Constants.ERR_USER_NOT_FOUND_ID_MSG;
import static com.open.numberManagement.util.Constants.ERR_USER_NOT_FOUND_LOGIN_MSG;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(Integer userId) {
		super(ERR_USER_NOT_FOUND + " - " + String.format(ERR_USER_NOT_FOUND_ID_MSG, userId));
	}
	
	public UserNotFoundException(String login) {
		super(ERR_USER_NOT_FOUND + " - " + String.format(ERR_USER_NOT_FOUND_LOGIN_MSG, login));
	}
}