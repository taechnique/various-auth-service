package io.teach.infrastructure.excepted;

import feign.Response;
import io.teach.infrastructure.http.body.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(final MethodArgumentNotValidException e) {

        final ServiceError serviceError = new AuthorizingException(ServiceError.INVALID_REQUEST_BODY).getServiceError();

        return ResponseEntity
                .status(serviceError.getStatus())
                .body(ErrorResponse.create(serviceError));
    }

    @ExceptionHandler(AuthorizingException.class)
    public ResponseEntity<StandardResponse> handleAuthorizingException(final AuthorizingException ex) {
        final ServiceError serviceError = ex.getServiceError();
        final ErrorResponse errorResponse = ErrorResponse.create(serviceError);
        final ResponseEntity<StandardResponse> response = ResponseEntity
                .status(serviceError.getStatus())
                .body(errorResponse);

        return response;
    }
}
