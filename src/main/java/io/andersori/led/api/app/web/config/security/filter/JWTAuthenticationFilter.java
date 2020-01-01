package io.andersori.led.api.app.web.config.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.andersori.led.api.app.web.config.security.jwt.JwtToken;
import io.andersori.led.api.app.web.config.security.jwt.SecurityConstants;
import io.andersori.led.api.app.web.config.security.util.UserRequest;
import io.andersori.led.api.domain.BeanUtil;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private JwtToken jwtToken;
	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		jwtToken = BeanUtil.getBean(JwtToken.class);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			UserRequest user = new ObjectMapper().readValue(request.getInputStream(), UserRequest.class);
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							user.getUsername(), user.getPassword()));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String token = jwtToken.generateToken(authResult);
		response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
	}
}
