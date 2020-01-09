package io.andersori.led.api.app.web.config.security.jwt;

import java.util.Date;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import io.andersori.led.api.app.web.config.security.util.SecurityUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Service
@ConfigurationProperties("jwt")
public class JWTToken {

	private String secret;
	private Long expiration;

	public String generateToken(Authentication authResult) {
		return JWT.create().withSubject(((User) authResult.getPrincipal()).getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + expiration * 1000))
				.sign(Algorithm.HMAC512(secret.getBytes()));
	}

	public String validateToken(String token) {
		return JWT.require(Algorithm.HMAC512(secret.getBytes())).build()
				.verify(token.replace(SecurityUtil.TOKEN_PREFIX, "")).getSubject();
	}

}
