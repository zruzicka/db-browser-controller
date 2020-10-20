package cz.zr.browser.controller;

import cz.zr.browser.dto.request.ConnectionRequestDto;
import cz.zr.browser.dto.response.ConnectionResponseDto;
import cz.zr.browser.dto.response.ConnectionsResponseDto;
import cz.zr.browser.dto.response.ErrorResponseDto;
import cz.zr.browser.service.ConnectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/connections")
public class ConnectionController {

  private final ConnectionService connectionService;

  @ApiOperation(value = "Create connection", nickname = "postConnection")
  @ApiResponses(value = {
    @ApiResponse(code = 201, message = "Registered", response = ConnectionResponseDto.class),
    @ApiResponse(code = 400, message = "Bad request", response = ErrorResponseDto.class),
    @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponseDto.class)
  })
  @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(value = HttpStatus.CREATED)
  public ConnectionResponseDto postConnection(@Valid @RequestBody ConnectionRequestDto requestDTO) {
    log.info("REST POST /v1/connections START");
    ConnectionResponseDto response = connectionService.createConnection(requestDTO);
    log.info("REST POST /v1/connections END, Response: {}", response);
    return response;
  }

  @ApiOperation(value = "Get connections", nickname = "getConnections")
  @ApiResponses(value = {
    @ApiResponse(code = 201, message = "Registered", response = ConnectionResponseDto.class),
    @ApiResponse(code = 400, message = "Bad request", response = ErrorResponseDto.class),
    @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponseDto.class)
  })
  @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public ConnectionsResponseDto getConnections() {
    log.info("REST GET /v1/connections START");
    ConnectionsResponseDto response = connectionService.getConnections();
    log.info("REST GET /v1/connections END, Response: {}", response);
    return response;
  }

}
