package info.rolandkrueger.userservice.controller;

import info.rolandkrueger.userservice.model.User;
import info.rolandkrueger.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Roland Krüger
 */
@RestController
@RequestMapping(value = "/user")
public class UserRestController implements UserService{

    @Autowired
    private UserService userService;

    @Override
    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public User findUserByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }
}
