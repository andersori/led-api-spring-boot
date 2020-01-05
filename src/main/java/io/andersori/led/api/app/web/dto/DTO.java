package io.andersori.led.api.app.web.dto;

public interface DTO<Entity, DtoT> {
	
	DtoT toDTO(Entity entity);
	
	Entity toEntity();
	
}
