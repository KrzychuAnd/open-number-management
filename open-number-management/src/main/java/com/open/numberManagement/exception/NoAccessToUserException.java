package com.open.numberManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NoAccessToUserException extends RuntimeException {

	public NoAccessToUserException(Integer userId) {
		super("You do not have access to user with id '" + userId + "'.");
	}
	
	public NoAccessToUserException(String login) {
		super("You do not have access to user with login '" + login + "'.");
	}
}