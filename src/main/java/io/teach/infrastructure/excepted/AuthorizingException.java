package io.teach.infrastructure.excepted;

public class AuthorizingException extends RuntimeException {

    private ServiceStatus error;

    public AuthorizingException(final ServiceStatus error){
        this.error = error;;
    }

    @Override
    public String getMessage() {
        return error.getCause();
    }

    public ServiceStatus getServiceError() {
        return this.error;
    }
}
