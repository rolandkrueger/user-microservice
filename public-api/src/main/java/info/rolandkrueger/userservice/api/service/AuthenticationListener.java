package info.rolandkrueger.userservice.api.service;

import com.google.common.base.Preconditions;
import info.rolandkrueger.userservice.api.UserServiceAPI;
import info.rolandkrueger.userservice.api.model.UserApiData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of a Spring {@link ApplicationListener} that listens to {@link InteractiveAuthenticationSuccessEvent}s
 * in order to mark the affected user as logged in in the service. When a user has successfully signed in with the service
 * the service has to be notified of this event. The service will then update the date for the user's last login accordingly
 * and mark the user as logged in.
 *
 * @author Roland Krüger
 */
@Component
public class AuthenticationListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationListener.class);

    private UserMicroserviceEndpointProvider endpointProvider;

    /**
     * Create a new AuthenticationListener with a specific {@link UserMicroserviceEndpointProvider}.
     *
     * @param endpointProvider an endpoint provider for the user micro-service so that the listener can access the service.
     */
    @Autowired
    public AuthenticationListener(UserMicroserviceEndpointProvider endpointProvider) {
        Preconditions.checkNotNull(endpointProvider);
        this.endpointProvider = endpointProvider;
    }

    /**
     * Will be called upon the successful login of a user, updates the user's status in the user micro-service. This
     * will update the date of the user's last login to the current timestamp and mark the user as logged in in the service.
     *
     * @param event event which indicates that a user has successfully signed in
     */
    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails user = (UserDetails) principal;
            Optional<UserApiData> userForLogin = UserServiceAPI.init(endpointProvider.getUserMicroserviceEndpoint())
                    .users()
                    .search()
                    .findUserForLogin(user.getUsername()).getData().stream().findFirst();
            if (!userForLogin.isPresent()) {
                LOG.warn("Received authentication success event for user '{}' but according to user service this user " +
                        "is not active/does not exist.", user.getUsername());
            } else {
                userForLogin.get().getResource().loggedIn();
            }
        }
    }
}
