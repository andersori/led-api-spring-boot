package io.andersori.led.api.domain.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.assistant.v2.model.MessageInput;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;

import redis.clients.jedis.Jedis;

@Service
public class IBMService {

	@Value("${ibm.api.assistant.id}")
	private String assistantId;

	private final Jedis redis;
	private final Assistant assistant;

	public IBMService(Jedis redis, Assistant assistant) {
		this.redis = redis;
		this.assistant = assistant;
	}

	public MessageResponse getMessage(String appUuid, String messageInput) {
		try {
			return assistant
					.message(new MessageOptions.Builder(assistantId, getSessionId(appUuid))
							.input(new MessageInput.Builder().messageType("text").text(messageInput).build()).build())
					.execute().getResult();
		} catch (NotFoundException e) {
			redis.del(appUuid);
			return assistant
					.message(new MessageOptions.Builder(assistantId, getSessionId(appUuid))
							.input(new MessageInput.Builder().messageType("text").text(messageInput).build()).build())
					.execute().getResult();
		}

	}

	private String getSessionId(String appUuid) {
		String sessionId = redis.get(appUuid);
		if (sessionId != null)
			return sessionId;
		sessionId = assistant.createSession(new CreateSessionOptions.Builder(assistantId).build()).execute().getResult()
				.getSessionId();
		redis.append(appUuid, sessionId);
		return sessionId;
	}
}
