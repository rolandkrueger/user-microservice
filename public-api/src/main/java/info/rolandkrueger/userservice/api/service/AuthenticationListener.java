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
 * @author Roland Krüger
 */
@Component
public class AuthenticationListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationListener.class);

    private UserMicroserviceEndpointProvider endpointProvider;

    @Autowired
    public AuthenticationListener(UserMicroserviceEndpointProvider endpointProvider) {
        Preconditions.checkNotNull(endpointProvider);
        this.endpointProvider = endpointProvider;
    }

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
                        "is not active.", user.getUsername());
            } else {
                userForLogin.get().getResource().loggedIn();
            }
        }
    }
}
