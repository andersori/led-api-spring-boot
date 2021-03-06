package io.andersori.led.api.resource.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.andersori.led.api.domain.entity.UserLed;

@Repository
public interface UserLedRepository extends JpaRepository<UserLed, Long>, JpaSpecificationExecutor<UserLed> {

	Optional<UserLed> findByUsername(String username);

	Optional<UserLed> findByUsernameAndPassword(String username, String password);

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("UPDATE UserLed u SET u.password = :password WHERE u.username = :username")
	Integer changePassword(String username, String password);

	@Query("SELECT u.password FROM UserLed AS u WHERE u.username = :username")
	String getPassword(String username);
}
