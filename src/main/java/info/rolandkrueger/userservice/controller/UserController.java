package info.rolandkrueger.userservice.controller;

import com.google.common.base.Preconditions;
import info.rolandkrueger.userservice.model.User;
import info.rolandkrueger.userservice.repository.UserRepository;
import info.rolandkrueger.userservice.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Roland Krüger
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "id", method = RequestMethod.PUT)
    public User updateUser(final User user) {
        Preconditions.checkNotNull(user);
        final User userFromDB = userRepository.findOne(user.getId());
        if (userFromDB == null) {
            throw new UserNotFoundException("Cannot update user " + user + ". User does not exist.");
        }
        userRepository.save(user);
        return user;
    }
}
