package io.andersori.led.api.domain.service;

import java.util.Optional;

import io.andersori.led.api.domain.entity.UserLed;

public interface UserLedService extends Service<UserLed> {

	public Optional<UserLed> get(String username);

	public Optional<UserLed> get(String username, String password);


}
