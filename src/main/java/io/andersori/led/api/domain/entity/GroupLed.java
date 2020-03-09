package io.andersori.led.api.domain.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "group_led")
@Getter
@Setter
@NoArgsConstructor
public class GroupLed {

	@Id
	@GeneratedValue
	@Column(name = "group_led_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "event_id", nullable = false)
	private Event event;

	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
	private Set<TeamLed> teams;

}
