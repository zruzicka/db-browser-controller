package cz.zr.browser.service;

import cz.zr.browser.dto.response.TableResponseDto;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class DbMetaDataService {

  private static final String COLUMN_TABLE_NAME = "TABLE_NAME";

  public Collection<TableResponseDto> getTables(Connection connection) throws SQLException {
    DatabaseMetaData databaseMetaData = connection.getMetaData();
    List<TableResponseDto> response = new ArrayList<>();
    ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"});
    while (resultSet.next()) {
      String tableName = resultSet.getString(COLUMN_TABLE_NAME);
      response.add(TableResponseDto.builder().name(tableName).build());
    }
    return response;
  }
}
