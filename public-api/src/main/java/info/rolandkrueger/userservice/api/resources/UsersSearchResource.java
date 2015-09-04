package info.rolandkrueger.userservice.api.resources;

import info.rolandkrueger.userservice.api._internal.SearchResource;
import org.springframework.hateoas.Link;

/**
 * @author Roland Krüger
 */
public class UsersSearchResource extends SearchResource {

    public UsersSearchResource(Link self) {
        super(self);
    }

    private Link getFindByUsernameLink() {
        return getLinkFor(getResponseEntity(), "findByUsername");
    }

    private Link getFindByRegistrationConfirmationToken() {
        return getLinkFor(getResponseEntity(), "findByRegistrationConfirmationToken");
    }
}
