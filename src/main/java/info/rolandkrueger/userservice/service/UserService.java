package info.rolandkrueger.userservice.service;

import info.rolandkrueger.userservice.model.User;

/**
 * @author Roland Krüger
 */
public interface UserService {

    User findUserByUsername(String username);

    User findByRegistrationConfirmationToken(String confirmationToken);
}
