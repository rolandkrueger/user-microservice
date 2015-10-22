package info.rolandkrueger.userservice.api.service;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Roland Krüger
 */
@Component
public class UserServiceAuthenticationProvider extends DaoAuthenticationProvider {
    @Autowired
    public UserServiceAuthenticationProvider(UserMicroserviceEndpointProvider endpointProvider) {
        setPasswordEncoder(new BCryptPasswordEncoder());
        setUserDetailsService(new UserDetailServiceImpl(endpointProvider));
    }
}
