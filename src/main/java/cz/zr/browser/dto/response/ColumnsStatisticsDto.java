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

  @ApiModelProperty
  private String min;

  @ApiModelProperty
  private String max;

  @ApiModelProperty
  private String avg;

}