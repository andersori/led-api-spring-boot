package io.andersori.led.api.app.web.config.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.andersori.led.api.app.web.config.security.jwt.JWTToken;
import io.andersori.led.api.app.web.config.security.util.SecurityUtil;
import io.andersori.led.api.app.web.config.security.util.UserRequest;
import io.andersori.led.api.app.web.response.ApiErrorResponse;
import io.andersori.led.api.domain.BeanUtil;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private ObjectMapper mapper;
	private JWTToken jwtToken;
	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		//setFilterProcessesUrl("/loginho");
		this.authenticationManager = authenticationManager;
		this.jwtToken = BeanUtil.getBean(JWTToken.class);
		this.mapper = BeanUtil.getBean(ObjectMapper.class);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			UserRequest user = new ObjectMapper().readValue(request.getInputStream(), UserRequest.class);
			return authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		} catch (IOException e) {
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String token = jwtToken.generateToken(authResult);
		response.addHeader(SecurityUtil.HEADER_STRING, SecurityUtil.TOKEN_PREFIX + token);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		
		SecurityContextHolder.clearContext();
		
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json");
		
		Throwable tr = failed.getCause();
		ApiErrorResponse error = new ApiErrorResponse();
		error.setClassType(tr != null ? tr.getClass().getCanonicalName() : failed.getClass().getCanonicalName());
		error.setMessage(tr != null ? tr.getMessage() : failed.getMessage());

		mapper.writeValue(response.getWriter(), error);
	}
}
