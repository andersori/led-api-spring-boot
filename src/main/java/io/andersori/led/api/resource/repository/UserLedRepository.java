package io.andersori.led.api.resource.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.andersori.led.api.domain.entity.UserLed;

@Repository
public interface UserLedRepository extends JpaRepository<UserLed, Long> {

	Optional<UserLed> findByUsername(String username);

	Optional<UserLed> findByUsernameAndPassword(String username, String password);

}
