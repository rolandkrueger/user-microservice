package info.rolandkrueger.userservice.api.resources;

import com.google.common.base.Preconditions;
import info.rolandkrueger.userservice.api._internal.AbstractRestClient;
import info.rolandkrueger.userservice.api._internal.RestApiConstants;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;

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

        ResponseEntity<String> entity = restTemplate.getForEntity(targetURI, String.class);
        authoritiesLink = readLink(targetURI, entity.getBody(), RestApiConstants.AUTHORITIES_RESOURCE, 1).get();
        usersLink = readLink(targetURI, entity.getBody(), RestApiConstants.USERS_RESOURCE, 1).get();
    }

    public AuthoritiesResource authorities(Integer page, Integer size) {
        return authorities().goToPage(page, size);
    }

    public AuthoritiesResource authorities() {
        return new AuthoritiesResource(authoritiesLink);
    }

    public UsersResource users() {
        return new UsersResource(usersLink);
    }

    public UsersResource users(Integer page, Integer size) {
        return users().goToPage(page, size);
    }
}
