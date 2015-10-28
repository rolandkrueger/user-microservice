package info.rolandkrueger.userservice.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Sub-class of {@link DaoAuthenticationProvider} that configures the user micro-service ({@link UserDetailServiceImpl})
 * as its user details service. This authentication provider uses a default {@link BCryptPasswordEncoder} as
 * password encoder. This is to ensure compatibility with the password encoding strategy used by the user service itself.
 *
 * @author Roland Krüger
 */
@Component
public class UserServiceAuthenticationProvider extends DaoAuthenticationProvider {

    /**
     * Constructor which is autowired with an instance of {@link UserMicroserviceEndpointProvider}.
     */
    @Autowired
    public UserServiceAuthenticationProvider(UserMicroserviceEndpointProvider endpointProvider) {
        setPasswordEncoder(new BCryptPasswordEncoder());
        setUserDetailsService(new UserDetailServiceImpl(endpointProvider));
    }
}
