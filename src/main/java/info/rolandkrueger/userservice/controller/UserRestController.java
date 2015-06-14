package info.rolandkrueger.userservice.controller;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import info.rolandkrueger.userservice.model.User;
import info.rolandkrueger.userservice.model.UserWithoutPasswordView;
import info.rolandkrueger.userservice.service.UserService;
import info.rolandkrueger.userservice.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Roland Krüger
 */
@RestController
@RequestMapping(value = "/user")
public class UserRestController {

    private UserService userService;

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    @JsonView(UserWithoutPasswordView.class)
    public User findUserByUsername(@PathVariable String username) {
        final User user = userService.findUserByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User " + username + " not found.");
        }
        return user;
    }

    @RequestMapping(method = RequestMethod.GET)
    @JsonView(UserWithoutPasswordView.class)
    public User findUserByRegistrationToken(@RequestParam("token") String confirmationToken) {
        final User user = userService.findByRegistrationConfirmationToken(confirmationToken);
        if (user == null) {
            throw new UserNotFoundException("No user found with registration confirmation token " + confirmationToken);
        }
        return user;
    }

    @RequestMapping(value = "/list/page/{page}/size/{size}/sort/{sort}", method = RequestMethod.GET)
    @JsonView(UserWithoutPasswordView.class)
    public List<User> getUsers(@PathVariable int page, @PathVariable int size, @PathVariable Sort.Direction sort) {
        return userService.getUserList(page, size, sort);
    }

    @RequestMapping(value = "/list/page/{page}/size/{size}", method = RequestMethod.GET)
    @JsonView(UserWithoutPasswordView.class)
    public List<User> getUsers(@PathVariable int page, @PathVariable int size) {
        return getUsers(page, size, Sort.Direction.ASC);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
