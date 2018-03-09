package com.open.numberManagement.exception.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import static com.google.common.base.Preconditions.checkNotNull;

import lombok.Data;

@Data
public class OpenNMExceptionResponse {

    private final long timestamp = System.currentTimeMillis();

    private Integer status;
    private String error;
    private String exception;
    private Integer businessCode;
    private String message;
    private String path;
    
    @JsonCreator
    public OpenNMExceptionResponse() {
    }
    
    @JsonCreator
    public OpenNMExceptionResponse(String message, Integer businessCode) {
    	checkNotNull(message, "message == NULL");
    	this.businessCode = businessCode;
        this.message = message;
    }
}
