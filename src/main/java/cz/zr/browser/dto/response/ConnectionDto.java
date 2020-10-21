package cz.zr.browser.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@ApiModel
public class ConnectionDto {

  @ApiModelProperty
  private Long id;

  @ApiModelProperty
  private String name;

  @ApiModelProperty
  private String hostname;

  @ApiModelProperty
  private Integer port;

  @ApiModelProperty
  private String databaseName;

  @ApiModelProperty
  private String username;

  @ApiModelProperty
  private String password;

  @ApiModelProperty
  private Date createdAt;

  /**
   * Connection password is excluded.
   */
  @Override
  public String toString() {
    return "ConnectionDto{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", hostname='" + hostname + '\'' +
      ", port=" + port +
      ", databaseName='" + databaseName + '\'' +
      ", username='" + username + '\'' +
      ", createdAt=" + createdAt +
      '}';
  }
}
