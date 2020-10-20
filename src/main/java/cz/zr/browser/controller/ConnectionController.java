package cz.zr.browser.controller;

import cz.zr.browser.dto.request.ConnectionRequestDTO;
import cz.zr.browser.dto.response.ConnectionResponseDTO;
import cz.zr.browser.dto.response.ErrorResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@RequestMapping("/v1/connections")
public class ConnectionController {

  @ApiOperation(value = "Create connection", nickname = "postConnection")
  @ApiResponses(value = {
    @ApiResponse(code = 201, message = "Registered", response = ConnectionResponseDTO.class),
    @ApiResponse(code = 400, message = "Bad request", response = ErrorResponseDTO.class),
    @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponseDTO.class)
  })
  @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(value = HttpStatus.CREATED)
  public ConnectionResponseDTO postConnection(@Valid @RequestBody ConnectionRequestDTO signupRequestDTO) {
    log.info("REST POST /v1/connections START");
    ConnectionResponseDTO response = new ConnectionResponseDTO();
    log.info("REST POST /v1/connections END, Response: {}", response);
    return response;
  }

  @ApiOperation(value = "Get connections", nickname = "getConnections")
  @ApiResponses(value = {
    @ApiResponse(code = 201, message = "Registered", response = ConnectionResponseDTO.class),
    @ApiResponse(code = 400, message = "Bad request", response = ErrorResponseDTO.class),
    @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponseDTO.class)
  })
  @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public ConnectionResponseDTO getConnections() {
    log.info("REST GET /v1/connections START");
    ConnectionResponseDTO response = new ConnectionResponseDTO();
    log.info("REST GET /v1/connections END, Response: {}", response);
    return response;
  }

}
