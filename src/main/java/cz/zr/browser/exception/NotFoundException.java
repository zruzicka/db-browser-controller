package cz.zr.browser.exception;

import cz.zr.browser.RestResponse;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

  private RestResponse response;

  public NotFoundException(RestResponse response) {
    super(response.getMessage());
    this.response = response;
  }

}
