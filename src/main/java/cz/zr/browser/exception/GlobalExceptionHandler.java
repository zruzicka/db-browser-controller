package cz.zr.browser.exception;

import cz.zr.browser.RestResponse;
import cz.zr.browser.dto.response.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.UnexpectedTypeException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({UnexpectedTypeException.class})
  public void handleUnexpectedTypeException(Exception e) throws Exception {
    log.error(getLogMessage(e), e);
    throw e;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({MethodArgumentNotValidException.class})
  public Map<String, String> handleValidationExceptions(
    MethodArgumentNotValidException ex) {
    log.error(getLogMessage(ex), ex);
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }

  @ExceptionHandler({Exception.class})
  public void handleInternalServerError(Exception e) throws Exception {
    log.error(getLogMessage(e), e);
    throw e;
  }

  @ExceptionHandler({NotFoundException.class})
  public ErrorResponseDto handleException(NotFoundException e, HttpServletRequest request, HttpServletResponse response) {
    log.error(getLogMessage(e), e);
    return prepareErrorResponse(e.getResponse(), request, response);
  }

  @ExceptionHandler({GenericInternalErrorException.class})
  public ErrorResponseDto handleException(GenericInternalErrorException e, HttpServletRequest request, HttpServletResponse response) {
    log.error(getLogMessage(e), e);
    return prepareErrorResponse(e.getResponse(), request, response);
  }

  private ErrorResponseDto prepareErrorResponse(RestResponse restError, HttpServletRequest request,
                                                HttpServletResponse response) {
    ErrorResponseDto errorResponse = restError.getErrorResponse();
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
