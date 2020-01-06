package io.andersori.led.api.app.web.dto;

public interface DTO<Entity, DtoType> {

	DtoType toDTO(Entity entity);

}
