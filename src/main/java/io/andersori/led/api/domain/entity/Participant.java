package io.andersori.led.api.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "participants")
@Getter
@Setter
@NoArgsConstructor
public class Participant {
	
	@Id
	@GeneratedValue
	@Column(name = "participant_id")
	private Long id;
	
	@Column(name = "name", length = 200, nullable = false)
	private String name;
	
	@Column(name = "secret", length = 6, nullable = false)
	private String secret;
	
	@ManyToOne
	@JoinColumn(name = "team_id")
	private TeamLed team;
	
	@ManyToOne
	@JoinColumn(name = "event_id")
	private Event event;
	
}
