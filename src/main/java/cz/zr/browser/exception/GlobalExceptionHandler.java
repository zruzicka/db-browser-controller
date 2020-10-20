package cz.zr.browser.exception;

import cz.zr.browser.RestResponse;
import cz.zr.browser.dto.response.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({GenericInternalErrorException.class})
  public ErrorResponseDto handleRegistrationException(GenericInternalErrorException e,
                                                      HttpServletRequest request, HttpServletResponse response) {
    log.error(getLogMessage(e), e);
    return prepareErrorResponse(e.getRestResponse(), request, response);
  }

  private ErrorResponseDto prepareErrorResponse(RestResponse restError, HttpServletRequest request,
                                                HttpServletResponse response) {
    ErrorResponseDto errorResponse = restError.getErrorResponse();
    errorResponse.setException(restError.getMessage());
    errorResponse.setPath(request.getServletPath());
    errorResponse.setStatus(restError.getStatus().value());
    errorResponse.setErrorCode(restError.getCode());
    response.setStatus(restError.getStatus().value());
    return errorResponse;
  }

  public static String getLogMessage(Exception e) {
    return String.format("Exception %s caused by: %s", e.getClass(), e.getMessage());
  }

}
