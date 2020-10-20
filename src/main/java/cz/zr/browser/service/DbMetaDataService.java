package cz.zr.browser.service;

import cz.zr.browser.dto.response.ColumnDto;
import cz.zr.browser.dto.response.TableDto;
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

  public Collection<TableDto> getTables(Connection connection) throws SQLException {
    DatabaseMetaData databaseMetaData = connection.getMetaData();
    List<TableDto> response = new ArrayList<>();
    ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"});
    while (resultSet.next()) {
      String tableName = resultSet.getString(COLUMN_TABLE_NAME);
      response.add(TableDto.builder().name(tableName).build());
    }
    return response;
  }

  public Collection<ColumnDto> getColumns(String tableName, Connection connection) throws SQLException {
    DatabaseMetaData databaseMetaData = connection.getMetaData();
    List<ColumnDto> response = new ArrayList<>();
    ResultSet columns = databaseMetaData.getColumns(null,null, tableName, null);
    while(columns.next()) {
      String columnName = columns.getString("COLUMN_NAME");
      String columnSize = columns.getString("COLUMN_SIZE");
      String datatype = columns.getString("DATA_TYPE");
      String isNullable = columns.getString("IS_NULLABLE");
      String isAutoIncrement = columns.getString("IS_AUTOINCREMENT");
      response.add(ColumnDto.builder().columnName(columnName).columnSize(columnSize).datatype(datatype).isNullable(isNullable).isAutoIncrement(isAutoIncrement).build());
    }
    return response;
  }

}
