package io.andersori.led.api.app.web.dto;

public interface Dto<Entity, DtoT> {
	
	DtoT toDto(Entity entity);
	
	Entity toEntity();
	
}
