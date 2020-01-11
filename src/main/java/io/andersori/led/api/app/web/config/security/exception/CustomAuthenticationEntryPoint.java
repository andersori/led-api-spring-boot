package io.andersori.led.api.app.web.config.security.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.andersori.led.api.app.web.response.ApiErrorResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Autowired
	private ObjectMapper mapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json");
		
		String classError = authException.getClass().getCanonicalName();
		String errorMsg = authException.getMessage();
		
		if(response.getHeader("CLASS_ERROR") != null) {
			classError = response.getHeader("CLASS_ERROR");
			response.setHeader("CLASS_ERROR", null);
		}
		
		if(response.getHeader("ERROR") != null) {
			errorMsg = response.getHeader("ERROR");
			response.setHeader("ERROR", null);
		}
		
		ApiErrorResponse error = new ApiErrorResponse();
		error.setClassType(classError);
		error.setMessage(errorMsg);

		mapper.writeValue(response.getWriter(), error);

	}

}
