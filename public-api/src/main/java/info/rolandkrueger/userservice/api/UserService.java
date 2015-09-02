package info.rolandkrueger.userservice.api;

import com.google.common.base.Preconditions;
import info.rolandkrueger.userservice.api._internal.AbstractRestClient;
import info.rolandkrueger.userservice.api._internal.RestApiConstants;
import org.springframework.hateoas.Link;

/**
 * @author Roland Krüger
 */
public class UserService extends AbstractRestClient {

    private Link authoritiesLink;
    private Link usersLink;

    UserService() {
    }

    public void init(String targetURI) {
        Preconditions.checkNotNull(targetURI);

        String rootURIData = restTemplate.getForObject(targetURI, String.class);
        authoritiesLink = readLink(targetURI, rootURIData, RestApiConstants.AUTHORITIES_RESOURCE, 1).get();
        usersLink = readLink(targetURI, rootURIData, RestApiConstants.USERS_RESOURCE, 1).get();
    }

    public AuthoritiesResource authorities(Integer page, Integer size) {
        return authorities().goToPage(page, size);
    }

    public AuthoritiesResource authorities() {
        return new AuthoritiesResource(authoritiesLink, false);
    }
}
