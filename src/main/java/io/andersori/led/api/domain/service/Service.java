package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;

public interface Service<T> {
	
	public Optional<T> save(T data);

	public void delete(Long id);

	public Optional<T> find(Long id);

	public List<T> find(int pageNumber, int pageSize);
	
	public List<T> findAll();

}
