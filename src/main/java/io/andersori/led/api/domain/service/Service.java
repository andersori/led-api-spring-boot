package io.andersori.led.api.domain.service;

import java.util.List;
import java.util.Optional;

public interface Service<T> {
	
	public Optional<T> save(T entity);

	public void delete(Long id);

	public Optional<T> get(Long id);

	public List<T> get(int pageNumber, int pageSize);
	
	public List<T> getAll();

}
