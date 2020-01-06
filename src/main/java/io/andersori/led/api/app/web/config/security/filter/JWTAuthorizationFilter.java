package io.andersori.led.api.app.web.config.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;

import io.andersori.led.api.app.web.config.security.jwt.JWTToken;
import io.andersori.led.api.app.web.config.security.util.SecurityUtil;
import io.andersori.led.api.domain.BeanUtil;
import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.domain.service.AccountService;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private static final Logger JWTLOGGER = LoggerFactory.getLogger(JWTAuthorizationFilter.class); 
	private JWTToken jwtToken;
	private AccountService accountService;
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
		jwtToken = BeanUtil.getBean(JWTToken.class);
		accountService = BeanUtil.getBean(AccountService.class);
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String header = request.getHeader(SecurityUtil.HEADER_STRING);

		if (header == null || !header.startsWith(SecurityUtil.TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}
	
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(SecurityUtil.HEADER_STRING);
		if (token != null) {
			try {
				String user = jwtToken.validateToken(token);
				if (user != null) {
					Account account = accountService.find(user);
					return new UsernamePasswordAuthenticationToken(user, account.getUser().getPassword(), SecurityUtil.getAuthorities(account.getRoles()));
				}
			} catch(JWTVerificationException e) {
				JWTLOGGER.info(e.getMessage());
			} catch (DomainException e) {
				JWTLOGGER.info(e.getMessage());
			}
		}
		return null;
	}
	
}
