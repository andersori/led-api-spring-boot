package io.andersori.led.api.resource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.andersori.led.api.domain.entity.UserMessage;

public interface MessageRepository extends JpaRepository<UserMessage, Long>, JpaSpecificationExecutor<UserMessage> {

}
