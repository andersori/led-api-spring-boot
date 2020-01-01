package io.andersori.led.api.domain.exception;

import org.eclipse.jetty.http.HttpStatus;

public class NotFoundException extends DomainException {

	private static final long serialVersionUID = 1L;

	public NotFoundException(Class<?> classType, String message) {
		super(HttpStatus.NOT_FOUND_404, classType, message);
	}

}
