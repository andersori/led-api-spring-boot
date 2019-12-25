package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;

import io.andersori.led.api.domain.entity.UserLed;

public interface UserLedService {

	public Optional<UserLed> save(UserLed user);

	public Optional<UserLed> delete(UserLed user);

	public Optional<UserLed> get(Long id);

	public Optional<UserLed> get(String username);

	public Optional<UserLed> get(String username, String password);

	public List<UserLed> get(int pageNumber, int pageSize);

	public List<UserLed> getAll();

}
