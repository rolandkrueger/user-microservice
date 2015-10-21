package info.rolandkrueger.userservice.api;

import info.rolandkrueger.userservice.api.resources.UserService;
import org.springframework.hateoas.Link;

/**
 * Main class for the user service public API. Use this class to get a handle on the service's root resource
 * represented by class {@link UserService}. Using this handle, you can navigate through the whole service through
 * a fluent API.
 *
 * @author Roland Krüger
 */
public final class UserServiceAPI {

    private UserServiceAPI() {
    }

    /**
     * Initialize the client with the user service's URL. Only this base URL needs to be known to be able to use the
     * service. All of the service's resources can be reached through the fluent API provided by the returned
     * {@link UserService} object.
     * <p/>
     * The service's base URL can also be dynamically provided by a
     * {@link info.rolandkrueger.userservice.api.service.UserMicroserviceEndpointProvider}.
     *
     * @param targetURI base URL of the user micro-service
     * @return a {@link UserService} object that provides access to the service's resources
     * @see info.rolandkrueger.userservice.api.service.UserMicroserviceEndpointProvider
     */
    public static UserService init(String targetURI) {
        UserService userService = new UserService(new Link(targetURI));
        userService.init();

        return userService;
    }
}
