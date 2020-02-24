package io.andersori.led.api.app.web.dto;

import java.time.LocalDateTime;

import io.andersori.led.api.domain.entity.UserMessage;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class UserMessageDTO implements DTO<UserMessage, UserMessageDTO> {
	
	@Setter(AccessLevel.PRIVATE)
	private Long id;
	
	private String message;
	private String appUuid;
	
	@Setter(AccessLevel.PRIVATE)
	private LocalDateTime createdAt;
	
	public UserMessageDTO(String message, String appUuid) {
		this.message = message;
		this.appUuid = appUuid;
	}
	
	@Override
	public UserMessageDTO toDTO(UserMessage entity) {
		id = entity.getId();
		message = entity.getMessage();
		appUuid = entity.getAppUuid();
		createdAt = entity.getCreatedAt();
		return this;
	}
	
	public UserMessage toEntity() {
		UserMessage message = new UserMessage();
		message.setAppUuid(appUuid);
		message.setMessage(this.message);
		return message;
	}
}
