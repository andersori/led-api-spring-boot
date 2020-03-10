package io.andersori.led.api.app.web.dto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import io.andersori.led.api.domain.entity.Account;
import io.andersori.led.api.domain.entity.RoleLed;
import io.andersori.led.api.domain.entity.UserLed;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AccountDTO implements DTO<Account, AccountDTO> {

	@Setter(AccessLevel.PRIVATE)
	private Long id;
	private String username;
	@Getter(AccessLevel.PRIVATE)
	private String password;
	private String firstName;
	private String lastName;
	@Setter(AccessLevel.PRIVATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime lastLogin;
	private String email;
	private Set<RoleLed> roles = new HashSet<RoleLed>(Arrays.asList(RoleLed.DEFAULT));

	@Override
	public AccountDTO toDTO(Account entity) {
		id = entity.getId();
		username = entity.getUser().getUsername();
		password = entity.getUser().getPassword();
		firstName = entity.getFirstName();
		lastName = entity.getLastName();
		lastLogin = entity.getLastLogin();
		email = entity.getEmail();
		roles = entity.getRoles();
		return this;
	}

	public Account toEntity() {
		roles.add(RoleLed.DEFAULT);
		Account entity = new Account();
		UserLed user = new UserLed();
		user.setId(id);
		user.setUsername(username);
		user.setPassword(password);
		entity.setId(id);
		entity.setFirstName(firstName);
		entity.setLastName(lastName);
		entity.setLastLogin(lastLogin);
		entity.setEmail(email);
		entity.setRoles(roles);
		entity.setUser(user);
		entity.setEvents(Arrays.asList());
		return entity;

	}

}
