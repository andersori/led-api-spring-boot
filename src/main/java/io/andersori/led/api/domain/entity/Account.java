package io.andersori.led.api.domain.entity;

import java.time.LocalDateTime;
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
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
public class Account {

	@Id
	private Long id;

	@MapsId
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private UserLed user;

	@Column(name = "name")
	private String name;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "last_login")
	private LocalDateTime lastLogin;
	
	@Column(name = "email")
	private String email;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "event_id")
	private List<Event> events;

	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.EAGER, targetClass = RoleLed.class)
	@CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role", length = 10, nullable = false)
	private Set<RoleLed> roles;

}
