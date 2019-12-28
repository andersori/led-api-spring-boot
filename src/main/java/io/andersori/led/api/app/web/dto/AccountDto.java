package io.andersori.led.api.app.web.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import io.andersori.led.api.domain.entity.Event;
import io.andersori.led.api.domain.entity.RoleLed;
import lombok.Data;

@Data
public class AccountDto {
	
	private Long id;
	private String username;
	private String firstName;
	private String lastName;
	private LocalDateTime lastLogin;
	private String email;
	private List<Event> events;
	private Set<RoleLed> roles;
	
}
