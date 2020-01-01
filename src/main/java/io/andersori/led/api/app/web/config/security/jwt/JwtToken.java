package io.andersori.led.api.app.web.config.security.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class JwtToken {

	private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	
	@Value("jwt.secret")
	private String secret;
	
	public String generateToken(Authentication authResult) {
		return JWT.create().withSubject(((User) authResult.getPrincipal()).getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
				.sign(Algorithm.HMAC512(secret.getBytes()));
	}

	public String validateToken(String token) {
		return JWT.require(Algorithm.HMAC512(secret.getBytes())).build()
				.verify(token.replace(SecurityConstants.TOKEN_PREFIX, "")).getSubject();
	}

}
