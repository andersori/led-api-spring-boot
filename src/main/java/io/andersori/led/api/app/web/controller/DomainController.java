package io.andersori.led.api.app.web.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;

import io.andersori.led.api.domain.exception.DomainException;

public interface DomainController<T> {

	T save(T data) throws DomainException;

	T find(Long id) throws DomainException;

	List<T> findAll(Pageable page, T filter);

	T update(Long id, T data) throws DomainException;
	
	void delete(Long id) throws DomainException;
	
}
