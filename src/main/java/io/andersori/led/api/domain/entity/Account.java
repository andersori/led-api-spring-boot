package io.andersori.led.api.domain.entity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "account")
@Getter
@Setter
public class Account {

	@Id
	private Long id;

	@MapsId
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private UserLed user;

	@Column(name = "first_name", length = 30, nullable = false)
	private String firstName;

	@Column(name = "last_name", length = 30)
	private String lastName;

	@Column(name = "last_login")
	private LocalDateTime lastLogin;

	@Column(name = "email", unique = true)
	private String email;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "event_id")
	private List<Event> events;

	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.EAGER, targetClass = RoleLed.class)
	@CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role", length = 10, nullable = false)
	private Set<RoleLed> roles;
	
	public Account() {
		roles = new HashSet<RoleLed>(Arrays.asList(RoleLed.DEFAULT));
	}

}
