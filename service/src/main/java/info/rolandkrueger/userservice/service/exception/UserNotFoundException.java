package info.rolandkrueger.userservice.service.exception;

import javax.ws.rs.NotFoundException;

/**
 * @author Roland Krüger
 */
public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
