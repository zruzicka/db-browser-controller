package cz.zr.browser.service;

import cz.zr.browser.dto.response.ColumnDto;
import cz.zr.browser.dto.response.ColumnsStatisticsDto;
import cz.zr.browser.dto.response.ColumnsStatisticsDto.ColumnsStatisticsDtoBuilder;
import cz.zr.browser.dto.response.TableDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
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

  public Collection<String> getSchemas(Connection connection) throws SQLException {
    Collection<String> response = new ArrayList<>();
    DatabaseMetaData databaseMetaData = connection.getMetaData();
    ResultSet schemas = databaseMetaData.getCatalogs();
    while (schemas.next()) {
      String tableSchema = schemas.getString(1);
      response.add(tableSchema);
    }
    return response;
  }

  public Collection<TableDto> getTables(Connection connection) throws SQLException {
    DatabaseMetaData databaseMetaData = connection.getMetaData();
    List<TableDto> response = new ArrayList<>();
    ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"});
    while (resultSet.next()) {
      String tableName = resultSet.getString(COLUMN_TABLE_NAME);
      response.add(TableDto.builder().tableName(tableName).build());
    }
    return response;
  }

  public Collection<ColumnDto> getColumns(String tableName, DataSource datasource, boolean calculateStatistics) throws SQLException {
    DatabaseMetaData databaseMetaData = datasource.getConnection().getMetaData();
    List<ColumnDto> response = new ArrayList<>();
    ResultSet columns = databaseMetaData.getColumns(null, null, tableName, null);
    while (columns.next()) {
      String columnName = columns.getString("COLUMN_NAME");
      String columnSize = columns.getString("COLUMN_SIZE");
      String datatype = columns.getString("DATA_TYPE"); // TODO provide value mapping to human-friendly and meaningful String/Enum.
      String isNullable = columns.getString("IS_NULLABLE");
      String isAutoIncrement = columns.getString("IS_AUTOINCREMENT");
      ColumnDto.ColumnDtoBuilder builder = ColumnDto.builder();
      if (calculateStatistics) {
        var jdbcTemplate = new JdbcTemplate(datasource);
        String sql = String.format("SELECT min(%s), max(%s), avg(%s) FROM %s", columnName, columnName, columnName, tableName);
        ColumnsStatisticsDtoBuilder statisticsBuilder = jdbcTemplate.queryForObject(
          sql, (rs, rowNum) ->
            ColumnsStatisticsDto.builder()
              .min(rs.getString(1))
              .max(rs.getString(2))
              .avg(rs.getString(3))
        );
        statisticsBuilder.median(calculateColumnMedian(tableName, columnName, datasource));
        builder.statistics(statisticsBuilder.build());
      }
      builder.columnName(columnName).columnSize(columnSize).datatype(datatype).isNullable(isNullable).isAutoIncrement(isAutoIncrement);
      response.add(builder.build());
    }
    return response;
  }

  private String calculateColumnMedian(String tableName, String columnName, DataSource datasource) {
    String medianRowIndexQuery = "SELECT CEIL(COUNT(*)/2) FROM " + tableName;
    Long medianRowIndex = queryForLong(datasource, medianRowIndexQuery);
    String medianSql = "SELECT max("+ columnName +") FROM (SELECT "+ columnName +" FROM "+ tableName +" ORDER BY "+ columnName +" limit "+medianRowIndex+") median;";
    return queryForString(datasource, medianSql);
  }

  private Long queryForLong(DataSource datasource, String sqlQuery) {
    var jtm = new JdbcTemplate(datasource);
    return jtm.queryForObject(sqlQuery, new Object[] {}, Long.class);
  }

  private String queryForString(DataSource datasource, String sqlQuery) {
    var jtm = new JdbcTemplate(datasource);
    return jtm.queryForObject(sqlQuery, new Object[] {}, String.class);
  }
}
