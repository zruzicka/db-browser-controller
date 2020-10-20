package cz.zr.browser.service;

import cz.zr.browser.dto.response.ConnectionDto;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DbConnection {

  /**
   * @param source Provides mysql connection details.
   * @return java.sql.Connection based on given connection details.
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  public static Connection getConnection(ConnectionDto source) throws SQLException {
    return getDataSource(source).getConnection();
  }

  public static DataSource getDataSource(ConnectionDto source) {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    String url = String.format("jdbc:mariadb://%s:%d/%s?useUnicode=yes;characterEncoding=UTF-8", source.getHostname(), source.getPort(), source.getDatabaseName());
    String username = source.getUsername();
    String password = source.getPassword();
    dataSource.setUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    return dataSource;
  }

}
