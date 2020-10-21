package cz.zr.browser.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel
public class ErrorResponseDto {

  private LocalDate timestamp;
  private Integer status;
  private String error;
  private String message;
  private String path;
  private Integer errorCode;

}
