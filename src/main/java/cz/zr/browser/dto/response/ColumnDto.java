package cz.zr.browser.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@ApiModel
public class ColumnDto {

  @ApiModelProperty
  private TableDto table;

  @ApiModelProperty
  private String columnName;

  @ApiModelProperty
  private String columnSize;

  @ApiModelProperty
  private String datatype;

  @ApiModelProperty
  private String isNullable;

  @ApiModelProperty
  private String isAutoIncrement;

  @ApiModelProperty
  private ColumnsStatisticsDto statistics;

}
