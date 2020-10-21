package cz.zr.browser.service;

import cz.zr.browser.RestResponse;
import cz.zr.browser.dto.request.ConnectionRequestDto;
import cz.zr.browser.dto.response.ConnectionDto;
import cz.zr.browser.dto.response.ConnectionsResponseDto;
import cz.zr.browser.entities.ConnectionEntity;
import cz.zr.browser.exception.NotFoundException;
import cz.zr.browser.mappers.GlobalMapper;
import cz.zr.browser.repository.ConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConnectionService {

  private final GlobalMapper mapper;

  private final ConnectionRepository connectionRepository;

  @Transactional
  public ConnectionDto createConnection(ConnectionRequestDto requestDTO) {
    ConnectionEntity connection = mapper.connectionRequestToConnectionEntity(requestDTO);
    connection = connectionRepository.save(connection);
    return mapper.connectionEntityToConnectionResponseDto(connection);
  }

  public ConnectionsResponseDto getConnections() {
    List<ConnectionEntity> connections = connectionRepository.findAll();
    List<ConnectionDto> availableConnections = mapper.connectionEntityToConnectionResponseDto(connections);
    return ConnectionsResponseDto.builder().connections(availableConnections).build();
  }

  public ConnectionDto getConnection(Long id) {
    ConnectionEntity connection = findConnection(id);
    return mapper.connectionEntityToConnectionResponseDto(connection);
  }

  @Transactional
  public ConnectionDto updateConnection(Long id, ConnectionRequestDto requestDTO) {
    ConnectionEntity connection = findConnection(id);
    connection.setName(requestDTO.getName());
    connection.setDatabaseName(requestDTO.getDatabaseName());
    connection.setHostname(requestDTO.getHostname());
    connection.setUsername(requestDTO.getUsername());
    connection.setPassword(requestDTO.getPassword());
    connection.setPort(requestDTO.getPort());
    connection = connectionRepository.save(connection);
    return mapper.connectionEntityToConnectionResponseDto(connection);
  }

  @Transactional
  public void deleteConnection(Long id) {
    ConnectionEntity connection = findConnection(id);
    connectionRepository.delete(connection);
  }

  private ConnectionEntity findConnection(Long id) {
    return connectionRepository.findById(id).orElseThrow(() -> new NotFoundException(RestResponse.NOT_FOUND));
  }
}
