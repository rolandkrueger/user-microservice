package info.rolandkrueger.userservice.api;

import info.rolandkrueger.userservice.api._internal.AbstractPagedResource;
import info.rolandkrueger.userservice.api.model.AuthorityDTO;
import org.springframework.hateoas.Link;

/**
 * @author Roland Krüger
 */
public class AuthoritiesResource extends AbstractPagedResource<AuthorityDTO> {

    private Link searchLink;

    AuthoritiesResource(Link self, boolean isAtPage) {
        super(self, isAtPage);
    }

    public AuthoritiesResource goToPage(Integer page, Integer size) {
        return new AuthoritiesResource(getLinkForPage(page, size), true);
    }

    public AuthoritiesResource next() {
        return new AuthoritiesResource(nextPageLink(), super.isAtPage);
    }

    public AuthoritiesResource previous() {
        return new AuthoritiesResource(previousPageLink(), super.isAtPage);
    }

    private String getSortByProperty(AuthoritiesSort sortBy) {
        return sortBy == null ? null : sortBy.getProperty();
    }

}
