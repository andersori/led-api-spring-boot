package io.andersori.led.api.app.web.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.andersori.led.api.app.web.response.ApiErrorResponse;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		ApiErrorResponse response = new ApiErrorResponse();
		response.setClassType(ex.getClass().getCanonicalName());
		response.setMessage(ex.getMessage());
		
		return new ResponseEntity<Object>(response, headers, status);
	}

}
