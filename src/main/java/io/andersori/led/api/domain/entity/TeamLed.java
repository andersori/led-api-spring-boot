package io.andersori.led.api.domain.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "team_led")
@Getter
@Setter
@NoArgsConstructor
public class TeamLed {

	@Id
	@GeneratedValue
	@Column(name = "team_led_id")
	private Long id;

	@Column(name = "name", length = 200, nullable = false)
	private String name;
	
	@Column(name = "verified", nullable = false)
	private boolean verified;
	
	@OneToMany(mappedBy = "team", fetch = FetchType.EAGER)
	private Set<Participant> participants;

	@Column(name = "score")
	private Integer score;

	@Column(name = "secret", length = 6, nullable = false)
	private String secret;

	@OneToOne
	@JoinColumn(name = "group_led_id")
	private GroupLed group;

	@OneToOne
	@JoinColumn(name = "event_id", nullable = false, updatable = false)
	private Event event;
	
}
