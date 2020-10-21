package cz.zr.browser.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@ApiModel
public class RowsResponseDto {

  @ApiModelProperty
  @Builder.Default
  private Collection<Map<String, Object>> rows = new ArrayList<>();
}
