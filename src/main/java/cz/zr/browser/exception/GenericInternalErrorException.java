package cz.zr.browser.exception;

import cz.zr.browser.RestResponse;
import lombok.Getter;

@Getter
public class GenericInternalErrorException extends RuntimeException {

  private RestResponse restResponse;

  public GenericInternalErrorException(RestResponse restResponse) {
    super(restResponse.getMessage());
    this.restResponse = restResponse;
  }

}
