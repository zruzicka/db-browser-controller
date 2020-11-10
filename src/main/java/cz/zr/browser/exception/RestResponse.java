package cz.zr.browser.exception;

import cz.zr.browser.dto.response.ErrorResponseDto;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;

public enum RestResponse {

  NOT_FOUND(HttpStatus.NOT_FOUND, 404, "Not found"),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "Internal server error"),
  DB_STRUCTURE_LOADING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 50001, "DB structure or data loading failed."),
  PASSWORD_ENCRYPTION_DECRYPTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 500, "Password encryption or decryption failed."),
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
