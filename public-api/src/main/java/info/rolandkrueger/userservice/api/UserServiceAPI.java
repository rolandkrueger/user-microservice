package info.rolandkrueger.userservice.api;

import info.rolandkrueger.userservice.api.resources.UserService;

/**
 * @author Roland Krüger
 */
public final class UserServiceAPI {

    private UserServiceAPI() {
    }

    public static UserService init(String targetURI) {
        UserService userService = new UserService();
        userService.init(targetURI);

        return userService;
    }
}
