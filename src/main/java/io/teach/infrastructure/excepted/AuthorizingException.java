package io.teach.infrastructure.excepted;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

public class AuthorizingException extends RuntimeException {

    private ServiceError error;

    public AuthorizingException(final ServiceError error){
        this.error = error;;
    }

    public ServiceError getServiceError() {
        return this.error;
    }
}
