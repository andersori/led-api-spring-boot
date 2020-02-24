package io.andersori.led.api.domain.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.andersori.led.api.app.web.dto.UserMessageDTO;
import io.andersori.led.api.domain.entity.UserMessage;
import io.andersori.led.api.domain.exception.DomainException;
import io.andersori.led.api.resource.repository.MessageRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageServiceImp implements MessageService {
	private final MessageRepository repo;

	@Override
	public UserMessage save(UserMessageDTO data) throws DomainException {
		try {
			return repo.save(data.toEntity());
		} catch (Exception e) {
			throw new DomainException(MessageService.class, e.getCause() != null ? e.getCause() : e);
		}
	}

	@Override
	public void delete(Long id) throws DomainException {
		throw new UnsupportedOperationException();
	}

	@Override
	public UserMessage find(Long id) throws DomainException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<UserMessage> findAll(Pageable page, UserMessageDTO filter) {
		throw new UnsupportedOperationException();
	}

}
