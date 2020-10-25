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
public class TableStatisticsDto {

  @ApiModelProperty(value = "Total amount of table records.")
  private Long recordsCount;

  @ApiModelProperty(value = "Total amount of columns in table records.")
  private Long columnsCount;

}
