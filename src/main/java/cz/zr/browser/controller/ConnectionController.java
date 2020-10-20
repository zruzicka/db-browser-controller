package cz.zr.browser.controller;

import cz.zr.browser.dto.request.ConnectionRequestDto;
import cz.zr.browser.dto.response.ConnectionDto;
import cz.zr.browser.dto.response.ConnectionsResponseDto;
import cz.zr.browser.dto.response.ErrorResponseDto;
import cz.zr.browser.service.ConnectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

  @ApiOperation(value = "Creates new DB connection record.", nickname = "postConnection")
  @ApiResponses(value = {
    @ApiResponse(code = 201, message = "Registered", response = ConnectionDto.class),
    @ApiResponse(code = 400, message = "Bad request", response = ErrorResponseDto.class),
    @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponseDto.class)
  })
  @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(value = HttpStatus.CREATED)
  public ConnectionDto postConnection(@Valid @RequestBody ConnectionRequestDto requestDTO) {
    log.info("REST POST /v1/connections START");
    ConnectionDto response = connectionService.createConnection(requestDTO);
    log.info("REST POST /v1/connections END, Response: {}", response);
    return response;
  }

  /**
   * Returns all available connections. Pagination, sorting and filtering is not supported yet.
   */
  @ApiOperation(value = "Returns all available DB connection records. (Pagination, sorting and filtering is not supported yet.)", nickname = "getConnections")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Success", response = ConnectionsResponseDto.class),
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

  @ApiOperation(value = "Updates DB connection record.", nickname = "updateConnection")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Updated", response = ConnectionDto.class),
    @ApiResponse(code = 400, message = "Bad request", response = ErrorResponseDto.class),
    @ApiResponse(code = 404, message = "Not found", response = ErrorResponseDto.class),
    @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponseDto.class)
  })
  @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public ConnectionDto updateConnection(
    @ApiParam(value = "Id of updated connection.", required = true) @PathVariable Long id,
    @Valid @RequestBody ConnectionRequestDto requestDTO) {
    log.info("REST PUT /v1/connections START");
    ConnectionDto response = connectionService.updateConnection(id, requestDTO);
    log.info("REST PUT /v1/connections END, Response: {}", response);
    return response;
  }

  @ApiOperation(value = "Deletes DB connection record.", nickname = "deleteConnection")
  @ApiResponses(value = {
    @ApiResponse(code = 204, message = "Deleted"),
    @ApiResponse(code = 404, message = "Not found", response = ErrorResponseDto.class),
    @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponseDto.class)
  })
  @DeleteMapping(value = "/{id}")
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void deleteConnection(@ApiParam(value = "Id of deleted connection.", required = true) @PathVariable Long id) {
    log.info("REST DELETE /v1/connections START");
    connectionService.deleteConnection(id);
    log.info("REST DELETE /v1/connections END");
  }
}
