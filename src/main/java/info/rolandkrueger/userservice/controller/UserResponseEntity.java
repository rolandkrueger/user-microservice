package info.rolandkrueger.userservice.controller;

import info.rolandkrueger.userservice.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Roland Krüger
 */
public class UserResponseEntity extends ResponseEntity<User> {
    public UserResponseEntity(User body, HttpStatus statusCode) {
        super(body, statusCode);
        body.clearPassword();
    }

    public UserResponseEntity(HttpStatus statusCode) {
        super(statusCode);
    }
}
