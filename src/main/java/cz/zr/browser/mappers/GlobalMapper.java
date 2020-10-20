package cz.zr.browser.mappers;

import cz.zr.browser.dto.request.ConnectionRequestDto;
import cz.zr.browser.dto.response.ConnectionDto;
import cz.zr.browser.entities.ConnectionEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GlobalMapper {

  ConnectionEntity connectionRequestToConnectionEntity(ConnectionRequestDto requestDto);

  ConnectionDto connectionEntityToConnectionResponseDto(ConnectionEntity entity);

  List<ConnectionDto> connectionEntityToConnectionResponseDto(List<ConnectionEntity> connections);
}
