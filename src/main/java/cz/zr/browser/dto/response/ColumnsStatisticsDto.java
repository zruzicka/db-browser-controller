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
public class ColumnsStatisticsDto {

  @ApiModelProperty(value = "Minimal value of related column.")
  private String min;

  @ApiModelProperty(value = "Maximal value of related column.")
  private String max;

  @ApiModelProperty(value = "Average value of related column.")
  private String avg;

  @ApiModelProperty(value = "Median value of related column.")
  private String median;

}
