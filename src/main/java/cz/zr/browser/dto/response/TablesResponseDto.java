package cz.zr.browser.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@ApiModel
public class TablesResponseDto {

  @ApiModelProperty
  private String databaseName;

  @ApiModelProperty
  private Collection<TableDto> tables;
}
