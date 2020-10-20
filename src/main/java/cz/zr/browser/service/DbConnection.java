package cz.zr.browser.service;

import cz.zr.browser.dto.response.ConnectionDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

  /**
   * @param source Provides mysql connection details.
   * @return java.sql.Connection based on given connection details.
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  public static Connection getConnection(ConnectionDto source) throws SQLException, ClassNotFoundException {
    Class.forName("com.mysql.cj.jdbc.Driver");
    String url = String.format("jdbc:mariadb://%s:%d/%s?useUnicode=yes;characterEncoding=UTF-8", source.getHostname(), source.getPort(), source.getDatabaseName());
    String username = source.getUsername();
    String password = source.getPassword();
    return DriverManager.getConnection(url, username, password);
  }

}
