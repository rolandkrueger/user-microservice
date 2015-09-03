package info.rolandkrueger.userservice.application;

import info.rolandkrueger.userservice.model.ServiceError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.ws.rs.NotFoundException;

/**
 * @author Roland Krüger
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ServiceError handleNotFoundException(NotFoundException notFoundException) {
        return new ServiceError(notFoundException);
    }
}
