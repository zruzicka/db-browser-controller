package cz.zr.browser.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@ApiModel
public class TableDto {

  @ApiModelProperty
  private String tableName;

  @ApiModelProperty(value = "Collection carries table rows. Each row is represented by Map<Key, Value> where Key represents columnName and Value is corresponding rowValue.")
  private Collection<Map<String, Object>> rows;
}
