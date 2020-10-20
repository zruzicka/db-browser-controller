package cz.zr.browser.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@ApiModel
public class SchemasResponseDto {

  @ApiModelProperty
  @Builder.Default
  private Collection<String> databaseSchemas = new ArrayList<>();
}
