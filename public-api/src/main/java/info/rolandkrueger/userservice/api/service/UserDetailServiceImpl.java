package info.rolandkrueger.userservice.api.service;

import com.google.common.base.Preconditions;
import info.rolandkrueger.userservice.api.UserServiceAPI;
import info.rolandkrueger.userservice.api.enums.UserProjections;
import info.rolandkrueger.userservice.api.model.UserApiData;
import info.rolandkrueger.userservice.api.resources.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

/**
 * @author Roland Krüger
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private UserMicroserviceEndpointProvider endpointProvider;

    @Autowired
    public UserDetailServiceImpl(UserMicroserviceEndpointProvider endpointProvider) {
        Preconditions.checkNotNull(endpointProvider);
        this.endpointProvider = endpointProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, RestClientException {
        UserService userService = UserServiceAPI.init(endpointProvider.getUserMicroserviceEndpoint());
        Optional<UserApiData> loadedUser = userService.users().search().findByUsername(username).getData().stream().findFirst();
        if (!loadedUser.isPresent()) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        return loadedUser.get().getResource().useProjection(UserProjections.FULL_DATA).read();
    }
}
