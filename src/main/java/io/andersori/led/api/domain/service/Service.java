package io.andersori.led.api.domain.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import io.andersori.led.api.domain.exception.DomainException;

public interface Service<T, DtoType> {

	T save(DtoType data) throws DomainException;

	void delete(Long id) throws DomainException;

	T find(Long id) throws DomainException;

	List<T> findAll(Pageable page, DtoType filter);

}
