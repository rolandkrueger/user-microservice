package info.rolandkrueger.userservice.controller;

import info.rolandkrueger.userservice.model.User;
import info.rolandkrueger.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author Roland Krüger
 */
@RestController
@RequestMapping(value = "/user")
public class UserRestController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public UserResponseEntity findUserByUsername(@PathVariable String username) {
        return buildResponseEntity(userService.findUserByUsername(username));
    }

    @RequestMapping(method = RequestMethod.GET)
    public UserResponseEntity findUserByRegistrationToken(@RequestParam("token") String confirmationToken) {
        return buildResponseEntity(userService.findByRegistrationConfirmationToken(confirmationToken));
    }

    private UserResponseEntity buildResponseEntity(User user) {
        if (user == null) {
            return new UserResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new UserResponseEntity(user, HttpStatus.OK);
    }
}
