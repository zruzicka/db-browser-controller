package cz.zr.browser.exception;

import cz.zr.browser.RestResponse;
import lombok.Getter;

@Getter
public class GenericInternalErrorException extends RuntimeException {

  private RestResponse response;

  public GenericInternalErrorException(RestResponse restResponse) {
    super(restResponse.getMessage());
    this.response = restResponse;
  }

}
