package io.andersori.led.api.domain.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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

	@OneToOne
	@JoinColumn(name = "event_id", nullable = false)
	private Event event;

	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@OneToMany(fetch = FetchType.LAZY)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinColumn(name = "group_led_id")
	private List<TeamLed> teams;

}
