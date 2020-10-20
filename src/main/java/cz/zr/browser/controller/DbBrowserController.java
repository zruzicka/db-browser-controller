package cz.zr.browser.controller;

import cz.zr.browser.dto.response.ColumnsResponseDto;
import cz.zr.browser.dto.response.ConnectionDto;
import cz.zr.browser.dto.response.ErrorResponseDto;
import cz.zr.browser.dto.response.RowsResponseDto;
import cz.zr.browser.dto.response.SchemasResponseDto;
import cz.zr.browser.dto.response.TableDto;
import cz.zr.browser.dto.response.TablesResponseDto;
import cz.zr.browser.service.ConnectionService;
import cz.zr.browser.service.DbBrowserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/connections")
public class DbBrowserController {

  private final ConnectionService connectionService;

  private final DbBrowserService dbBrowserService;

  /**
   * Returns all available schemas of selected DB connection. Pagination, sorting and filtering is not supported.
   */
  @ApiOperation(value = "Returns all available schemas of selected DB connection. (Pagination, sorting and filtering is not supported.)",
    nickname = "getSchemas")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Success", response = SchemasResponseDto.class),
    @ApiResponse(code = 404, message = "Not found", response = ErrorResponseDto.class),
    @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponseDto.class)
  })
  @GetMapping(value = "/{id}/schemas", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public SchemasResponseDto getSchemas(@ApiParam(value = "Id of selected DB connection.", required = true) @PathVariable Long id) {
    log.info("REST GET /v1/connections/{}/schemas START", id);
    ConnectionDto datasource = connectionService.getConnection(id);
    SchemasResponseDto response = dbBrowserService.getSchemas(datasource);
    log.info("REST GET /v1/connections/{}/schemas END, Response: {}", id, response);
    return response;
  }

  /**
   * Returns all available tables of selected DB connection. Pagination, sorting and filtering is not supported.
   */
  @ApiOperation(value = "Returns all available tables of selected DB connection. (Pagination, sorting and filtering is not supported.)",
    nickname = "getTables")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Success", response = TablesResponseDto.class),
    @ApiResponse(code = 404, message = "Not found", response = ErrorResponseDto.class),
    @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponseDto.class)
  })
  @GetMapping(value = "/{id}/tables", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public TablesResponseDto getTables(@ApiParam(value = "Id of selected DB connection.", required = true) @PathVariable Long id) {
    log.info("REST GET /v1/connections/{}/tables START", id);
    ConnectionDto datasource = connectionService.getConnection(id);
    TablesResponseDto response = dbBrowserService.getTables(datasource);
    log.info("REST GET /v1/connections/{}/tables END, Response: {}", id, response);
    return response;
  }

  /**
   * Returns all available columns of selected DB connection and DB table. Pagination, sorting and filtering is not supported.
   */
  @ApiOperation(value = "Returns all available columns of selected DB connection and DB table. (Pagination, sorting and filtering is not supported.)",
    nickname = "getColumns")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Success", response = ColumnsResponseDto.class),
    @ApiResponse(code = 404, message = "Not found", response = ErrorResponseDto.class),
    @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponseDto.class)
  })
  @GetMapping(value = "/{id}/tables/{tableName}/columns", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public ColumnsResponseDto getColumns(
    @ApiParam(value = "Id of selected DB connection.", required = true) @PathVariable Long id,
    @ApiParam(value = "Selected table name.", required = true) @PathVariable String tableName) {
    log.info("REST GET /v1/connections/{}/tables/{}/columns START", id, tableName);
    ConnectionDto datasource = connectionService.getConnection(id);
    ColumnsResponseDto response = dbBrowserService.getColumns(tableName, datasource);
    log.info("REST GET /v1/connections/{}/tables/{}/columns END, Response: {}", id, tableName, response);
    return response;
  }

  /**
   * Returns all available rows of selected DB connection and DB table. Pagination, sorting and filtering is not supported.
   */
  @ApiOperation(value = "Returns all available rows of selected DB connection and DB table. (Pagination, sorting and filtering is not supported.)",
    nickname = "getTableDataPreview")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Success", response = TableDto.class),
    @ApiResponse(code = 404, message = "Not found", response = ErrorResponseDto.class),
    @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponseDto.class)
  })
  @GetMapping(value = "/{id}/tables/{tableName}/preview", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public TableDto getTableDataPreview(
    @ApiParam(value = "Id of selected DB connection.", required = true) @PathVariable Long id,
    @ApiParam(value = "Selected table name.", required = true) @PathVariable String tableName) {
    log.info("REST GET /v1/connections/{}/tables/{}/preview START", id, tableName);
    ConnectionDto datasource = connectionService.getConnection(id);
    RowsResponseDto rowsResponse = dbBrowserService.getDataPreview(tableName, datasource);
    TableDto response = TableDto.builder().tableName(tableName).rows(rowsResponse.getRows()).build();
    log.info("REST GET /v1/connections/{}/tables/{}/preview END, Response: {}", id, tableName, rowsResponse);
    return response;
  }
}
