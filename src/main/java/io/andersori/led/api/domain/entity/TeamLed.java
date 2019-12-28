package io.andersori.led.api.domain.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
	
	@Column(name = "name", length = 200)
	private String name;
	
	@Column(name = "participants", length = 400)
	private String participants;
	
	@Column(name = "score")
	private Integer score;
	
	@Column(name = "secret", length = 5)
	private String secret;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "group_led_id", nullable = true)
	private GroupLed group;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "event_id", nullable = false)
	private Event event;
}