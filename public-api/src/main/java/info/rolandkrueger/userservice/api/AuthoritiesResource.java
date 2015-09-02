package info.rolandkrueger.userservice.api;

import info.rolandkrueger.userservice.api._internal.AbstractPagedResource;
import info.rolandkrueger.userservice.api.model.AuthorityApiData;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;

/**
 * @author Roland Krüger
 */
public class AuthoritiesResource extends AbstractPagedResource<AuthorityApiData> {

    private Link searchLink;

    AuthoritiesResource(Link self, boolean isAtPage) {
        super(self, isAtPage);
    }

    @Override
    protected ParameterizedTypeReference<PagedResources<AuthorityApiData>> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<PagedResources<AuthorityApiData>>() {
        };
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
