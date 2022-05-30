package io.teach.infrastructure.excepted;

import io.teach.infrastructure.http.body.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(final MethodArgumentNotValidException e) {

        final ServiceStatus serviceStatus = new AuthorizingException(ServiceStatus.INVALID_REQUEST_BODY).getServiceError();

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

        return response;
    }
}
