package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.andersori.led.api.domain.entity.UserLed;
import io.andersori.led.api.resource.repository.UserLedRepository;

@Service
public class UserLedServiceImp implements UserLedService {
	
	private UserLedRepository userRepository;
	
	@Autowired
	public UserLedServiceImp(UserLedRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Optional<UserLed> save(UserLed entity) {
		try {
			return Optional.of(userRepository.save(entity));
		} catch(Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public void delete(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public Optional<UserLed> get(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public Optional<UserLed> get(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public Optional<UserLed> get(String username, String password) {
		return userRepository.findByUsernameAndPassword(username, password);
	}

	@Override
	public List<UserLed> get(int pageNumber, int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("user_id"));
		return userRepository.findAll(page).getContent();
	}

	@Override
	public List<UserLed> getAll() {
		return userRepository.findAll();
	}
	
}
