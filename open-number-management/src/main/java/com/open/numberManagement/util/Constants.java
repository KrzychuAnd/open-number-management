package com.open.numberManagement.util;

import org.springframework.stereotype.Component;

@Component
public class Constants {

	//Permissions
	public static final String ADMINISTRATOR_PERMISSION = "ADMIN_PERM";
	
	//Statuses
	public static final Integer RESOURCE_EMPTY_STATUS_ID = 0;
	public static final String RESOURCE_STATUS_RETIRED = "RETIRED";
	public static final String RESOURCE_STATUS_AVAILABLE = "AVAILABLE";
	
	//Add Resource result statuses
	public static final String ADD_RESULT_OK = "OK";
	public static final String ADD_RESULT_NOK = "NOK";
	
	//Error Codes
	public static final Integer IS_VALID = 0;
	public static final Integer ERR_RESOURCE_NAME_LENGTH_INVALID = 100;
	public static final Integer ERR_RESOURCE_NAME_PREFIX_INVALID = 200;
	public static final Integer ERR_RESOURCE_STATUS_LIFECYCLE_IS_NOT_ALLOWED = 300;
	
}
