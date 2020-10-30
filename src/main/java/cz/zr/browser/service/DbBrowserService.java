package cz.zr.browser.service;

import cz.zr.browser.RestResponse;
import cz.zr.browser.dto.response.ColumnDto;
import cz.zr.browser.dto.response.ColumnsResponseDto;
import cz.zr.browser.dto.response.ConnectionDto;
import cz.zr.browser.dto.response.RowsResponseDto;
import cz.zr.browser.dto.response.SchemasResponseDto;
import cz.zr.browser.dto.response.TableDto;
import cz.zr.browser.dto.response.TableStatisticsDto;
import cz.zr.browser.dto.response.TableStatisticsDto.TableStatisticsDtoBuilder;
import cz.zr.browser.dto.response.TablesResponseDto;
import cz.zr.browser.exception.GenericInternalErrorException;
import cz.zr.browser.exception.GlobalExceptionHandler;
import cz.zr.browser.utils.DbConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class DbBrowserService {

  private final DbMetaDataService dbMetaDataService;

  public SchemasResponseDto getSchemas(ConnectionDto connectionDto) {
    SchemasResponseDto response = SchemasResponseDto.builder().build();
    try (Connection connection = DbConnection.getConnection(connectionDto)){
      response.getDatabaseSchemas().addAll(dbMetaDataService.getSchemas(connection));
    } catch (SQLException e) {
      logAndThrowStructureLoadingFailResponse(connectionDto, e);
    }
    return response;
  }

  public TablesResponseDto getTables(ConnectionDto connectionDto) {
    Collection<TableDto> tables = null;
    try (Connection connection = DbConnection.getConnection(connectionDto)){
      tables = dbMetaDataService.getTables(connection);
    } catch (SQLException e) {
      logAndThrowStructureLoadingFailResponse(connectionDto, e);
    }
    return TablesResponseDto.builder().tables(tables).databaseName(connectionDto.getDatabaseName()).build();
  }

  public ColumnsResponseDto getColumns(String tableName, ConnectionDto connectionDto) {
    return getColumns(tableName, connectionDto, false);
  }

  private ColumnsResponseDto getColumns(String tableName, ConnectionDto connectionDto, boolean calculateStatistics) {
    Collection<ColumnDto> columns = null;
    try {
      DataSource dataSource = DbConnection.getDataSource(connectionDto);
      columns = dbMetaDataService.getColumns(tableName, dataSource, calculateStatistics);
    } catch (SQLException e) {
      logAndThrowStructureLoadingFailResponse(connectionDto, e);
    }
    return ColumnsResponseDto.builder().columns(columns).build();
  }

  public RowsResponseDto getDataPreview(String tableName, ConnectionDto connectionDto) {
    RowsResponseDto response = RowsResponseDto.builder().build();
    String dataPreviewQuery = "SELECT * FROM " + tableName;
    try {
      DataSource dataSource = DbConnection.getDataSource(connectionDto);
      DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
      TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
      transactionTemplate.execute(action -> {
        var jtm = new JdbcTemplate(dataSource);
        var rawRows = jtm.queryForList(dataPreviewQuery);
        rawRows.forEach(row -> {
          response.getRows().add(row);
        });
        return true;
      });
    } catch (DataAccessException e) {
      logAndThrowStructureLoadingFailResponse(connectionDto, e);
    }
    return response;
  }

  private void logAndThrowStructureLoadingFailResponse(ConnectionDto connectionDto, Exception e) {
    String message = String.format("Structure or data loading failed for connectionDto %s. Reason: %s", connectionDto, GlobalExceptionHandler.getLogMessage(e));
    log.error(message, e);
    throw new GenericInternalErrorException(RestResponse.DB_STRUCTURE_LOADING_FAILED);
  }

  public TableStatisticsDto getTableStatistics(String tableName, String schemaName, ConnectionDto connectionDto) {
    TableStatisticsDtoBuilder builder = TableStatisticsDto.builder();
    String recordsCountQuery = "SELECT count(*) FROM " + tableName;
    String columnsCountQuery = "SELECT COUNT(*) AS `columns` FROM `information_schema`.`columns` WHERE " +
      "`table_schema` = '" + schemaName + "' AND " +
      "`table_name` =  '" + tableName + "';";
    try {
      DataSource dataSource = DbConnection.getDataSource(connectionDto);
      DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
      TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
      transactionTemplate.execute(action -> {
        Long recordsCount = queryForLong(recordsCountQuery, dataSource);
        Long columnsCount = queryForLong(columnsCountQuery, dataSource);
        builder.recordsCount(recordsCount).columnsCount(columnsCount);
        return true;
      });
    } catch (DataAccessException e) {
      logAndThrowStructureLoadingFailResponse(connectionDto, e);
    }
    return builder.build();
  }

  private Long queryForLong(String sqlQuery, DataSource dataSource) {
    var jtm = new JdbcTemplate(dataSource);
    return jtm.queryForObject(sqlQuery, new Object[] {}, Long.class);
  }

  public ColumnsResponseDto getColumnsStatistics(String tableName, ConnectionDto connectionDto) {
    return getColumns(tableName, connectionDto, true);
  }

}
