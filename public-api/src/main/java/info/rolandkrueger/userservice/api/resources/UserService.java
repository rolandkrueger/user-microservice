package info.rolandkrueger.userservice.api.resources;

import info.rolandkrueger.userservice.api._internal.AbstractPagedResource;
import info.rolandkrueger.userservice.api._internal.RestApiConstants;
import org.springframework.hateoas.Link;

/**
 * Base resource of the user service. Serves as central hub of the service and provides links to all available
 * resource types.
 *
 * @author Roland Krüger
 */
public class UserService extends EmptyResource {

    private Link authoritiesLink;
    private Link usersLink;
    private Link registrationsLink;

    public UserService(Link self) {
        super(self);
    }

    /**
     * Initializes the user service: reads all resource links from the resource document obtained from the server.
     */
    public void init() {
        authoritiesLink = getLinkFor(getResponseEntity(), RestApiConstants.AUTHORITIES_RESOURCE);
        usersLink = getLinkFor(getResponseEntity(), RestApiConstants.USERS_RESOURCE);
        registrationsLink = getLinkFor(getResponseEntity(), RestApiConstants.REGISTRATIONS_RESOURCE);
    }

    /**
     * Provides the paged {@link AuthoritiesResource} with the given page parameters.
     *
     * @param page the required page number
     * @param size the required page size
     * @return paged resource for authorities
     * @see AbstractPagedResource#getMetadata()
     */
    public AuthoritiesResource authorities(Integer page, Integer size) {
        return authorities().goToPage(page, size);
    }

    /**
     * Provides the paged {@link AuthoritiesResource} with default paging parameters applied. These are configured by the user service.
     */
    public AuthoritiesResource authorities() {
        return new AuthoritiesResource(authoritiesLink);
    }

    /**
     * Provides the paged {@link UsersResource} with default paging parameters applied. These are configured by the user service.
     */
    public UsersResource users() {
        return new UsersResource(usersLink);
    }

    /**
     * Provides the paged {@link UsersResource} with the given page parameters.
     *
     * @param page the required page number
     * @param size the required page size
     * @return paged resource for users
     * @see AbstractPagedResource#getMetadata()
     */
    public UsersResource users(Integer page, Integer size) {
        return users().goToPage(page, size);
    }

    /**
     * Provides the {@link UserRegistrationsResource}.
     */
    public UserRegistrationsResource userRegistrations() {
        return new UserRegistrationsResource(registrationsLink);
    }
}
