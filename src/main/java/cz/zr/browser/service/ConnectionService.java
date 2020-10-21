package cz.zr.browser.service;

import cz.zr.browser.RestResponse;
import cz.zr.browser.dto.request.ConnectionRequestDto;
import cz.zr.browser.dto.response.ConnectionDto;
import cz.zr.browser.dto.response.ConnectionsResponseDto;
import cz.zr.browser.entities.ConnectionEntity;
import cz.zr.browser.exception.GenericInternalErrorException;
import cz.zr.browser.exception.GlobalExceptionHandler;
import cz.zr.browser.exception.NotFoundException;
import cz.zr.browser.mappers.GlobalMapper;
import cz.zr.browser.repository.ConnectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionService {

  private final GlobalMapper mapper;

  private final ConnectionRepository connectionRepository;

  private final CipherService cipherService;

  private SecretKey secretKey;

  @PostConstruct
  public void init() throws UnsupportedEncodingException, NoSuchAlgorithmException {
    secretKey = cipherService.createDefaultKey();
  }

  /**
   * @return {@link ConnectionDto} excluding connection password.
   */
  @Transactional
  public ConnectionDto createConnection(ConnectionRequestDto requestDTO) {
    ConnectionEntity connection = mapper.connectionRequestToConnectionEntity(requestDTO);
    // TODO Apply salt usage.
    connection.setPassword(encrypt(requestDTO.getPassword()));
    connection = connectionRepository.save(connection);
    ConnectionDto connectionDto = mapper.connectionEntityToConnectionResponseDto(connection);
    // Password value shall not be provided in response.
    connectionDto.setPassword(null);
    return connectionDto;
  }

  /**
   * @return {@link ConnectionsResponseDto} excluding connection passwords.
   */
  public ConnectionsResponseDto getConnections() {
    List<ConnectionEntity> connections = connectionRepository.findAll();
    List<ConnectionDto> availableConnections = mapper.connectionEntityToConnectionResponseDto(connections);
    // Password values shall not be provided in response.
    // BE_AWARE/TO_DECIDE: Password String remains in memory until GC execution
    // so if we want to prevent passwords capturing via memory dump, then characters array should be used and cleaned properly instead.
    availableConnections.forEach(connection -> connection.setPassword(null));
    return ConnectionsResponseDto.builder().connections(availableConnections).build();
  }

  /**
   * @return {@link ConnectionDto} including connection password.
   */
  public ConnectionDto getConnection(Long id) {
    ConnectionEntity connection = findConnection(id);
    ConnectionDto connectionDto = mapper.connectionEntityToConnectionResponseDto(connection);
    connectionDto.setPassword(decrypt(connectionDto.getPassword()));
    return connectionDto;
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

  protected String decrypt(String encrypted) {
    try {
      return cipherService.decrypt(encrypted, secretKey);
    } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
      log.error(GlobalExceptionHandler.getLogMessage(e), e);
      throw new GenericInternalErrorException(RestResponse.PASSWORD_ENCRYPTION_DECRYPTION_FAILED);
    }
  }

  protected String encrypt(String plainText) {
    try {
      return cipherService.encrypt(plainText, secretKey);
    } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
      log.error(GlobalExceptionHandler.getLogMessage(e), e);
      throw new GenericInternalErrorException(RestResponse.PASSWORD_ENCRYPTION_DECRYPTION_FAILED);
    }
  }
}
