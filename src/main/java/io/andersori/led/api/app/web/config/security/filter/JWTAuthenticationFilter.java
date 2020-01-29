package io.andersori.led.api.app.web.config.security.filter;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.andersori.led.api.app.web.config.security.jwt.JWTToken;
import io.andersori.led.api.app.web.config.security.util.SecurityUtil;
import io.andersori.led.api.app.web.config.security.util.UserRequest;
import io.andersori.led.api.app.web.controller.util.PathConfig;
import io.andersori.led.api.app.web.response.ApiErrorResponse;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.service.AccountService;

@Component
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

	private ObjectMapper mapper;
	private JWTToken jwtToken;
	private AccountService accountService;

	@Autowired
	public JWTAuthenticationFilter(ObjectMapper mapper, JWTToken jwtToken, AccountService accountService) {
		setFilterProcessesUrl(PathConfig.VERSION + PathConfig.PUBLIC_PATH + "/login");
		this.jwtToken = jwtToken;
		this.mapper = mapper;
		this.accountService = accountService;
	}

	@Autowired
	@Override
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			UserRequest user = new ObjectMapper().readValue(request.getInputStream(), UserRequest.class);
			Authentication auth = this.getAuthenticationManager()
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(auth);
			return auth;
		} catch (IOException e) {
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String token = jwtToken.generateToken(authResult);
		response.addHeader(SecurityUtil.HEADER_STRING, SecurityUtil.TOKEN_PREFIX + token);

		try {
			accountService.changeLastLogin(SecurityContextHolder.getContext().getAuthentication().getName(),
					LocalDateTime.now());
		} catch (DomainException e) {
			LOGGER.error(e.getMessage());
		}
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
