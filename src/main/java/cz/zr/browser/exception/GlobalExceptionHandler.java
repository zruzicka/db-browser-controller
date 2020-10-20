package cz.zr.browser.exception;

import cz.zr.browser.RestResponse;
import cz.zr.browser.dto.response.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({GenericInternalErrorException.class})
  public ErrorResponseDTO handleRegistrationException(GenericInternalErrorException e,
                                                      HttpServletRequest request, HttpServletResponse response) {
    log.error(getLogMessage(e), e);
    return prepareErrorResponse(e.getRestResponse(), request, response);
  }

  private ErrorResponseDTO prepareErrorResponse(RestResponse restError, HttpServletRequest request,
                                                HttpServletResponse response) {
    ErrorResponseDTO errorResponse = restError.getErrorResponse();
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
