package info.rolandkrueger.userservice.api;

import info.rolandkrueger.userservice.api.resources.UserService;
import org.springframework.hateoas.Link;

/**
 * @author Roland Krüger
 */
public final class UserServiceAPI {

    private UserServiceAPI() {
    }

    public static UserService init(String targetURI) {
        UserService userService = new UserService(new Link(targetURI));
        userService.init();

        return userService;
    }
}
