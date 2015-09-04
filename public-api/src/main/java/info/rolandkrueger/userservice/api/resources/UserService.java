package info.rolandkrueger.userservice.api.resources;

import info.rolandkrueger.userservice.api._internal.AbstractResource;
import info.rolandkrueger.userservice.api._internal.RestApiConstants;
import info.rolandkrueger.userservice.api._internal.model.AbstractBaseApiData;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;

/**
 * @author Roland Krüger
 */
public class UserService extends EmptyResource {

    private Link authoritiesLink;
    private Link usersLink;

    public UserService(Link self) {
        super(self);
    }

    public void init() {
        authoritiesLink = getLinkFor(getResponseEntity(), RestApiConstants.AUTHORITIES_RESOURCE);
        usersLink = getLinkFor(getResponseEntity(), RestApiConstants.USERS_RESOURCE);
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
