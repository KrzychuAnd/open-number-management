package com.open.numberManagement.exception;

import static com.open.numberManagement.util.Constants.ERR_RESOURCE_TYPE_NOT_FOUND;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_TYPE_NOT_FOUND_RES_TYPE_ID_MSG;
import static com.open.numberManagement.util.Constants.ERR_RESOURCE_TYPE_NOT_FOUND_RES_TYPE_NAME_MSG;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceTypeNotFoundException extends OpenNMAbstractRuntimeException {

	public ResourceTypeNotFoundException(Integer resTypeId) {
		super(String.format(ERR_RESOURCE_TYPE_NOT_FOUND_RES_TYPE_ID_MSG, resTypeId));
	}
	
	public ResourceTypeNotFoundException(String resTypeName) {
		super(String.format(ERR_RESOURCE_TYPE_NOT_FOUND_RES_TYPE_NAME_MSG, resTypeName));
	}

	@Override
	protected void setCommonFields() {
		this.setHttpStatus(HttpStatus.NOT_FOUND);
		this.setBusinessCode(ERR_RESOURCE_TYPE_NOT_FOUND);
	}

}