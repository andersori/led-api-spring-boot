package io.andersori.led.api.domain.entity;

import org.springframework.security.core.GrantedAuthority;

public enum RoleLed implements GrantedAuthority {
	DEFAULT, ADMIN, TEACHER;

	@Override
	public String getAuthority() {
		return this.name();
	}
}
