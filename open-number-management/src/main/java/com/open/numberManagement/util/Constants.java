package com.open.numberManagement.util;

import org.springframework.stereotype.Component;

@Component
public class Constants {

	//URL constants
	public static final String URL_VERSION_PATH = "v1/";
	public static final String URL_RESOURCE_PATH = "resources/";
	public static final String URL_USER_PATH = "users/";
	
	public static final String URL_VERSION_AND_RESOURCE_PATH = URL_VERSION_PATH + URL_RESOURCE_PATH;
	public static final String URL_VERSION_AND_USER_PATH = URL_VERSION_PATH + URL_USER_PATH;
	
	//Limits
	public static final Integer NUMBER_OF_RESOURCES_TO_GENERATE_LIMIT = 50;
	
	//Permissions
	public static final String ADMINISTRATOR_PERMISSION = "ADMIN_PERM";
	
	//Statuses
	public static final Integer RESOURCE_EMPTY_STATUS_ID = 0;
	public static final String RESOURCE_STATUS_RETIRED = "RETIRED";
	public static final String RESOURCE_STATUS_AVAILABLE = "AVAILABLE";
	public static final String RESOURCE_STATUS_RESERVED = "RESERVED";
	
	//Resource result statuses
	public static final String RESOURCE_RESULT_OK = "OK";
	public static final String RESOURCE_RESULT_NOK = "NOT OK";
	public static final String RESOURCE_RESULT_GENERATE_OK_WITH_WARNING = "OK WITH WARNING";
	
	//Resource result messages
	public static final String RESOURCE_GENERATE_RESULT_MSG = "Number of Resources created with success %s of %s requested.";
	
	//Error Codes
	public static final Integer IS_VALID = 0;
	public static final Integer ERR_RESOURCE_NAME_LENGTH_INVALID = 100;
	public static final Integer ERR_RESOURCE_NAME_PREFIX_INVALID = 105;
	public static final Integer ERR_RESOURCE_STATUS_LIFECYCLE_IS_NOT_ALLOWED = 110;

	public static final Integer ERR_RESOURCE_GENERATE_MAX_NUMBER_EXCEEDED = 200;
	public static final Integer ERR_RESOURCE_GENERATE_MAX_NUM_EXISTS = 205;
	
	//Error messages
	public static final String ERR_RESOURCE_GENERATE_MAX_NUMBER_EXCEEDED_MSG = "Generate Maximum numbers exceeded. Limit is " + NUMBER_OF_RESOURCES_TO_GENERATE_LIMIT;
	public static final String ERR_RESOURCE_STATUS_LIFECYCLE_IS_NOT_ALLOWED_MSG = "Status transition from %s to %s is not allowed";
	public static final String ERR_RESOURCE_NAME_LENGTH_INVALID_MSG = "Resource name length is not valid. Proper length of Resource Type %s is %s";
	public static final String ERR_RESOURCE_NAME_PREFIX_INVALID_MSG = "Resource prefix is not valid. Propert perfix of Resource Type %s is %s";
	public static final String ERR_RESOURCE_GENERATE_MAX_NUM_EXISTS_MSG = "Maximum Resource number of '%s' Resource Type already exists!";
	
	//Info messages
	public static final String INFO_RESOURCE_AUTO_GENERATED = "Resource automatically generated via REST API";
	public static final String INFO_RESOURCE_ADDED_WITH_SUCCESS = "Resource added";
	
}
