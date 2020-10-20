package cz.zr.browser.service;

import cz.zr.browser.RestResponse;
import cz.zr.browser.dto.response.ColumnDto;
import cz.zr.browser.dto.response.ColumnsResponseDto;
import cz.zr.browser.dto.response.ConnectionDto;
import cz.zr.browser.dto.response.RowsResponseDto;
import cz.zr.browser.dto.response.SchemasResponseDto;
import cz.zr.browser.dto.response.TableDto;
import cz.zr.browser.dto.response.TablesResponseDto;
import cz.zr.browser.exception.GenericInternalErrorException;
import cz.zr.browser.exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class DbBrowserService {

  private final DbMetaDataService dbMetaDataService;

  public SchemasResponseDto getSchemas(ConnectionDto datasource) {
    SchemasResponseDto response = SchemasResponseDto.builder().build();
    try {
      Connection connection = DbConnection.getConnection(datasource);
      response.getDatabaseSchemas().addAll(dbMetaDataService.getSchemas(connection));
    } catch (SQLException e) {
      logAndThrowStructureLoadingFailResponse(datasource, e);
    }
    return response;
  }

  public TablesResponseDto getTables(ConnectionDto datasource) {
    Collection<TableDto> tables = null;
    try {
      Connection connection = DbConnection.getConnection(datasource);
      tables = dbMetaDataService.getTables(connection);
    } catch (SQLException e) {
      logAndThrowStructureLoadingFailResponse(datasource, e);
    }
    return TablesResponseDto.builder().tables(tables).databaseName(datasource.getDatabaseName()).build();
  }

  public ColumnsResponseDto getColumns(String tableName, ConnectionDto datasource) {
    Collection<ColumnDto> columns = null;
    try {
      Connection connection = DbConnection.getConnection(datasource);
      columns = dbMetaDataService.getColumns(tableName, connection);
    } catch (SQLException e) {
      logAndThrowStructureLoadingFailResponse(datasource, e);
    }
    return ColumnsResponseDto.builder().columns(columns).build();
  }

  public RowsResponseDto getDataPreview(String tableName, ConnectionDto datasource) {
    RowsResponseDto response = RowsResponseDto.builder().build();
    try {
      var jtm = new JdbcTemplate(DbConnection.getDataSource(datasource));
      String sql = "SELECT * FROM " + tableName;
      var rawRows = jtm.queryForList(sql);
      rawRows.forEach(row -> {
        response.getRows().add(row);
      });
    } catch (DataAccessException e) {
      logAndThrowStructureLoadingFailResponse(datasource, e);
    }
    return response;
  }

  private void logAndThrowStructureLoadingFailResponse(ConnectionDto datasource, Exception e) {
    String message = String.format("Structure or data loading failed for datasource %s. Reason: %s", datasource, GlobalExceptionHandler.getLogMessage(e));
    log.error(message, e);
    throw new GenericInternalErrorException(RestResponse.DB_STRUCTURE_LOADING_FAILED);
  }
}
