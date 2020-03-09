package io.andersori.led.api.domain.entity;

import java.time.LocalDate;
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
@Table(name = "event")
@Getter
@Setter
@NoArgsConstructor
public class Event {

	@Id
	@GeneratedValue
	@Column(name = "event_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "owner_id", nullable = false)
	private Account owner;

	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@Column(name = "date")
	private LocalDate date;

	@Column(name = "description", length = 300)
	private String description;

	@OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
	private Set<GroupLed> groups;

}
