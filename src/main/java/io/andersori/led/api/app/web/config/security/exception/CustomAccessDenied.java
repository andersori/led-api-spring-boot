package io.andersori.led.api.app.web.config.security.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.andersori.led.api.app.web.response.ApiErrorResponse;

@Component
public class CustomAccessDenied implements AccessDeniedHandler {

	@Autowired
	private ObjectMapper mapper;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType("application/json");

		ApiErrorResponse error = new ApiErrorResponse();
		error.setClassType(accessDeniedException.getClass().getCanonicalName());
		error.setMessage(accessDeniedException.getMessage());

		mapper.writeValue(response.getWriter(), error);
	}

}
