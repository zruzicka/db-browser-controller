package cz.zr.browser.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@ApiModel
public class ConnectionsResponseDto {

  @ApiModelProperty
  private List<ConnectionResponseDto> connections;
}
