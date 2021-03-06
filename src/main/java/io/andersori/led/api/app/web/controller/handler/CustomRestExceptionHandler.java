package io.andersori.led.api.app.web.controller.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.andersori.led.api.app.web.response.ApiErrorResponse;
import io.andersori.led.api.domain.exception.DomainException;

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

	@ExceptionHandler(DomainException.class)
	private ResponseEntity<Object> handleDomainException(DomainException ex, WebRequest request) {

		ApiErrorResponse response = new ApiErrorResponse();
		response.setClassType(
				ex.getCause() != null ? ex.getCause().getClass().getCanonicalName() : ex.getClass().getCanonicalName());
		response.setMessage(ex.getMessage());

		return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.valueOf(ex.getHttpStatusCode()),
				request);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiErrorResponse response = new ApiErrorResponse();
		response.setClassType(ex.getClass().getCanonicalName());
		response.setMessage(ex.getMessage());

		return new ResponseEntity<Object>(response, headers, status);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiErrorResponse response = new ApiErrorResponse();
		response.setClassType(ex.getClass().getCanonicalName());
		response.setMessage(ex.getMessage());

		return new ResponseEntity<Object>(response, headers, status);
	}
}
