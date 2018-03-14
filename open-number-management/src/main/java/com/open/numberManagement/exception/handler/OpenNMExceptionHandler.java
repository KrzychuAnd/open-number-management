package com.open.numberManagement.exception.handler;

import static com.open.numberManagement.util.Constants.ERR_BAD_REQUEST;

import java.net.URI;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.open.numberManagement.exception.OpenNMAbstractRuntimeException;
import com.open.numberManagement.exception.response.OpenNMExceptionResponse;

@ControllerAdvice
public class OpenNMExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public @ResponseBody OpenNMExceptionResponse handle(HttpServletResponse response, MethodArgumentNotValidException exception) {
		OpenNMExceptionResponse exceptionResponse = new OpenNMExceptionResponse();
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();
		
		HttpStatus status = Optional
				.ofNullable(AnnotationUtils.getAnnotation(exception.getClass(), ResponseStatus.class))
				.map(ResponseStatus::value).orElse(HttpStatus.BAD_REQUEST);
		
		response.setStatus(status.value());
		
		exceptionResponse.setStatus(status.value());
		exceptionResponse.setError(status.getReasonPhrase());
		exceptionResponse.setException(exception.getClass().getName());
		exceptionResponse.setBusinessCode(ERR_BAD_REQUEST);
		exceptionResponse.setMessage(exception.getMessage());
		exceptionResponse.setPath(uri.getPath());		
		
		return exceptionResponse;
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public @ResponseBody OpenNMExceptionResponse handle(HttpServletResponse response, MethodArgumentTypeMismatchException exception) {
		OpenNMExceptionResponse exceptionResponse = new OpenNMExceptionResponse();
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();
		
		HttpStatus status = Optional
				.ofNullable(AnnotationUtils.getAnnotation(exception.getClass(), ResponseStatus.class))
				.map(ResponseStatus::value).orElse(HttpStatus.BAD_REQUEST);
		
		response.setStatus(status.value());
		
		exceptionResponse.setStatus(status.value());
		exceptionResponse.setError(status.getReasonPhrase());
		exceptionResponse.setException(exception.getClass().getName());
		exceptionResponse.setBusinessCode(ERR_BAD_REQUEST);
		exceptionResponse.setMessage(exception.getMessage());
		exceptionResponse.setPath(uri.getPath());		
		
		return exceptionResponse;
	}	

	@ExceptionHandler(OpenNMAbstractRuntimeException.class)
	public @ResponseBody OpenNMExceptionResponse handle(HttpServletResponse response, OpenNMAbstractRuntimeException exception) {
		OpenNMExceptionResponse exceptionResponse = new OpenNMExceptionResponse();
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();
		
		response.setStatus(exception.getHttpStatus().value());
		
		exceptionResponse.setStatus(exception.getHttpStatus().value());
		exceptionResponse.setError(exception.getHttpStatus().getReasonPhrase());
		exceptionResponse.setException(exception.getClass().getName());
		exceptionResponse.setBusinessCode(exception.getBusinessCode());
		exceptionResponse.setMessage(exception.getMessage());
		exceptionResponse.setPath(uri.getPath());
		
		return exceptionResponse;
	}
}
