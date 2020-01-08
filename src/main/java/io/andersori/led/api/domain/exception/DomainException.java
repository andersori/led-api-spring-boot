package io.andersori.led.api.domain.exception;

import org.eclipse.jetty.http.HttpStatus;

import lombok.Getter;

@Getter
public class DomainException extends Exception {

	private static final long serialVersionUID = 1L;
	private int httpStatusCode = HttpStatus.BAD_GATEWAY_502;
	private Class<?> classType;

	public DomainException(Class<?> classType, String message) {
		super(message);
		this.classType = classType;
	}
	
	public DomainException(Class<?> classType, Throwable cause) {
		super(cause.getMessage(), cause);
		this.classType = classType;
	}

	public DomainException(int httpStatusCode, Class<?> classType, String message) {
		super(message);
		this.httpStatusCode = httpStatusCode;
		this.classType = classType;
	}
}
