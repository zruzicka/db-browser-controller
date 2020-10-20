package cz.zr.browser.service;

import cz.zr.browser.dto.request.ConnectionRequestDto;
import cz.zr.browser.dto.response.ConnectionResponseDto;
import cz.zr.browser.dto.response.ConnectionsResponseDto;
import cz.zr.browser.entities.ConnectionEntity;
import cz.zr.browser.mappers.GlobalMapper;
import cz.zr.browser.repository.ConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConnectionService {

  private final GlobalMapper mapper;

  private final ConnectionRepository connectionRepository;

  public ConnectionResponseDto createConnection(ConnectionRequestDto requestDTO) {
    ConnectionEntity connection = mapper.connectionRequestToConnectionEntity(requestDTO);
    connection = connectionRepository.save(connection);
    return mapper.connectionEntityToConnectionResponseDto(connection);
  }

  public ConnectionsResponseDto getConnections() {
    List<ConnectionEntity> connections = connectionRepository.findAll();
    List<ConnectionResponseDto> availableConnections = mapper.connectionEntityToConnectionResponseDto(connections);
    return ConnectionsResponseDto.builder().connections(availableConnections).build();
  }
}
