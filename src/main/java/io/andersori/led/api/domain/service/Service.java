package io.andersori.led.api.domain.service;

import java.util.List;

import io.andersori.led.api.domain.exception.DomainException;

public interface Service<T> {

	T save(T data) throws DomainException;

	void delete(Long id);

	T find(Long id) throws DomainException;

	List<T> find(int pageNumber, int pageSize);

	List<T> findAll();

}
