package io.andersori.led.api.domain.exception;

import org.eclipse.jetty.http.HttpStatus;

public class ConflictException extends DomainException {

	private static final long serialVersionUID = 1L;

	public ConflictException(Class<?> classType, String message) {
		super(HttpStatus.CONFLICT_409, classType, message);
	}

}
