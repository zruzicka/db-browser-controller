package cz.zr.browser.service;

import cz.zr.browser.RestResponse;
import cz.zr.browser.dto.response.ConnectionDto;
import cz.zr.browser.dto.response.TableResponseDto;
import cz.zr.browser.dto.response.TablesResponseDto;
import cz.zr.browser.exception.GenericInternalErrorException;
import cz.zr.browser.exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class DbBrowserService {

  private final DbMetaDataService dbMetaDataService;

  public TablesResponseDto getTables(ConnectionDto datasource) {
    Collection<TableResponseDto> tables = null;
    try {
      Connection connection = DbConnection.getConnection(datasource);
      tables = dbMetaDataService.getTables(connection);
    } catch (SQLException e) {
      String message = String.format("Tables loading failed for datasource %s. Reason: %s", datasource, GlobalExceptionHandler.getLogMessage(e));
      log.error(message, e);
      throw new GenericInternalErrorException(RestResponse.DB_TABLES_LOADING_FAILED);
    } catch (ClassNotFoundException e) {
      log.error(GlobalExceptionHandler.getLogMessage(e), e);
      throw new GenericInternalErrorException(RestResponse.INTERNAL_SERVER_ERROR);
    }
    return TablesResponseDto.builder().tables(tables).build();
  }

}
