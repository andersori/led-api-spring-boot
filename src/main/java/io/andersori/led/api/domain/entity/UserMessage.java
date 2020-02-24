package io.andersori.led.api.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_message")
@Getter
@Setter
@NoArgsConstructor
public class UserMessage {

	@Id
	@GeneratedValue
	@Column(name = "message_id")
	private Long id;

	@Column(name = "message", length = 500, nullable = false)
	private String message;

	@Column(name = "app_uuid", length = 40, nullable = false)
	private String appUuid;

	@Setter(AccessLevel.PRIVATE)
	@Column(name = "created_at", columnDefinition = "TIMESTAMP")
	private LocalDateTime createdAt = LocalDateTime.now();
	
}
