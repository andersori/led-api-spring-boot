package io.andersori.led.api.app.web.config.security.filter;

import java.io.IOException;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;

import io.andersori.led.api.app.web.config.security.jwt.JWTToken;
import io.andersori.led.api.app.web.config.security.util.SecurityUtil;
import io.andersori.led.api.app.web.controller.util.PathConfig;
import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.service.AccountService;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

	private JWTToken jwtToken;
	private AccountService accountService;

	@Autowired
	public JWTAuthorizationFilter(JWTToken jwtToken, AccountService accountService) {
		this.jwtToken = jwtToken;
		this.accountService = accountService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String token = request.getHeader(SecurityUtil.HEADER_STRING);

		if (token != null) {
				if( token.startsWith(SecurityUtil.TOKEN_PREFIX)) {
					try {
						String username = jwtToken.validateToken(token);
						if (username != null) {
							Account account = accountService.find(username);
							UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username,
									account.getUser().getPassword(), SecurityUtil.getAuthorities(account.getRoles()));
							auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
							SecurityContextHolder.getContext().setAuthentication(auth);
						}
					} catch (JWTVerificationException e) {
						LOGGER.warn(e.getMessage());
						throw new AuthenticationException(e.getMessage());
					} catch (DomainException e) {
						LOGGER.error(e.getMessage());
						throw new AuthenticationException(e.getMessage());
					}
		
				} else {
					LOGGER.warn("JWT_TOKEN_DOES_NOT_START_WITH_BEARER_STRING");
					throw new AuthenticationException("JWT Token does not start with Bearer string.");
				}
			} else {
				LOGGER.warn("JWT_TOKEN_DOES_NOT_START_WITH_BEARER_STRING");
				throw new AuthenticationException("JWT Token not provided.");
			}

		chain.doFilter(request, response);
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		if(request.getRequestURI().startsWith(PathConfig.VERSION + PathConfig.PUBLIC_PATH)) {
			return true;
		}
		return false;
	}
}
