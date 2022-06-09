package io.teach.infrastructure.excepted;

import io.teach.infrastructure.http.body.StandardResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(final MethodArgumentNotValidException e) {

        final ServiceStatus serviceStatus = new AuthorizingException(ServiceStatus.INVALID_PARAMETER).getServiceError();
        System.out.println("Invalid Method Argument: "+ e);
        return ResponseEntity
                .status(serviceStatus.getStatus())
                .body(ErrorResponse.create(serviceStatus));
    }

    @ExceptionHandler(AuthorizingException.class)
    public ResponseEntity<StandardResponse> handleAuthorizingException(final AuthorizingException ex) {
        final ServiceStatus serviceStatus = ex.getServiceError();
        final ErrorResponse errorResponse = ErrorResponse.create(serviceStatus);
        final ResponseEntity<StandardResponse> response = ResponseEntity
                .status(serviceStatus.getStatus())
                .body(errorResponse);

        log.error("Exception at: ",ex);

        return response;
    }
}
