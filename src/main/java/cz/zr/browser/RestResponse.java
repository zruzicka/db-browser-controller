package cz.zr.browser;

import cz.zr.browser.dto.response.ErrorResponseDto;
import cz.zr.browser.exception.GenericInternalErrorException;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;

public enum RestResponse {

  OK(HttpStatus.OK, 200, "Success"),

  NOT_FOUND(HttpStatus.NOT_FOUND, 404, "Not found"),

  FORBIDDEN_REQUEST(HttpStatus.FORBIDDEN, 403, "Request is not permitted based on provided/requested details."),

  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "Internal server error"),
  ;

  private HttpStatus status;
  private Integer code;
  private String message;

  RestResponse(HttpStatus status, int code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public ErrorResponseDto getErrorResponse() {
    return ErrorResponseDto.builder().status(this.status.value()).message(this.message).build();
  }

  public String toString() {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("status", this.status.value());
      jsonObject.put("code", this.code);
      jsonObject.put("message", this.message);
    } catch (JSONException e) {
      throw new GenericInternalErrorException(RestResponse.INTERNAL_SERVER_ERROR);
    }
    return jsonObject.toString();
  }

}
