package cz.zr.browser.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ConnectionRequestDto {

  @NotNull
  @ApiModelProperty(required = true)
  private String name;

  @NotNull
  @ApiModelProperty(required = true)
  private String hostname;

  @NotNull
  @Range(min=0, max=65535)
  @ApiModelProperty(required = true)
  private Integer port;

  @NotNull
  @ApiModelProperty(required = true)
  private String databaseName;

  @NotNull
  @ApiModelProperty(required = true)
  private String username;

  @NotNull
  @ApiModelProperty(required = true)
  private String password;

}
