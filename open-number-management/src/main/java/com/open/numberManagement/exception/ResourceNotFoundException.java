package com.open.numberManagement.exception;

import static com.open.numberManagement.util.Constants.ERR_RESOURCE_NOT_FOUND;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_NOT_FOUND_RES_ID_MSG;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_NOT_FOUND_RES_NAME_MSG;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_NOT_FOUND_RES_TYPE_ID_MSG;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_NOT_FOUND_RES_TYPE_NAME_MSG;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_NOT_FOUND_RES_TYPE_ID_AND_RES_STAT_ID_NAME_MSG;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends OpenNMAbstractRuntimeException {

	public ResourceNotFoundException(Integer resId) {
		super(String.format(ERR_RESOURCE_NOT_FOUND_RES_ID_MSG, resId));
	}
	
	public ResourceNotFoundException(String name) {
		super(String.format(ERR_RESOURCE_NOT_FOUND_RES_NAME_MSG, name));
	}
	
	public ResourceNotFoundException(Integer resTypeId, boolean isResType) {
		super(String.format(ERR_RESOURCE_NOT_FOUND_RES_TYPE_ID_MSG, resTypeId));
	}
	
	public ResourceNotFoundException(String resType, boolean isResType) {
		super(String.format(ERR_RESOURCE_NOT_FOUND_RES_TYPE_NAME_MSG, resType));
	}	
	
	public ResourceNotFoundException(Integer resTypeId, Integer resStatusId) {
		super(String.format(ERR_RESOURCE_NOT_FOUND_RES_TYPE_ID_AND_RES_STAT_ID_NAME_MSG, resTypeId, resStatusId));
	}

	@Override
	protected void setCommonFields() {
		this.setHttpStatus(HttpStatus.NOT_FOUND);
		this.setBusinessCode(ERR_RESOURCE_NOT_FOUND);
	}	
}