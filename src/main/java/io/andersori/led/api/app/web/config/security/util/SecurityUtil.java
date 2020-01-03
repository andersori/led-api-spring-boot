package io.andersori.led.api.app.web.config.security.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.andersori.led.api.domain.entity.RoleLed;

public class SecurityUtil {
	
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";

	public static Collection<? extends GrantedAuthority> getAuthorities(Collection<RoleLed> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (RoleLed role : roles) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
		}
		return authorities;
	}
}
