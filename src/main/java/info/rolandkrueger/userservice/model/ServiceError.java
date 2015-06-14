package info.rolandkrueger.userservice.model;

/**
 * @author Roland Krüger
 */
public class ServiceError {
    private String error;
    private String message;

    public ServiceError(Exception exception) {
        this.message = exception.getMessage();
        this.error = exception.getClass().getSimpleName();
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
