package info.rolandkrueger.userservice.api;

import info.rolandkrueger.userservice.api._internal.AbstractRestClient;
import info.rolandkrueger.userservice.api._internal.RestApiConstants;
import info.rolandkrueger.userservice.api.exceptions.UnexpectedAPIFormatException;
import org.springframework.hateoas.Link;

import java.util.Optional;

/**
 * @author Roland Krüger
 */
public class AuthoritiesResource extends AbstractRestClient {

    private Link selfLink;
    private Link searchLink;
    private Optional<Link> nextLinkOptional;
    private Optional<Link> prevLinkOptional;

    AuthoritiesResource(Link selfLink) {
        assert selfLink != null;
        this.selfLink = selfLink;
    }

    public AuthoritiesResource loadLinks() {
        loadSelf(selfLink);
        return this;
    }

    public AuthoritiesData load() {
        return load(null, null, null, null);
    }

    public AuthoritiesData load(Integer page, Integer size) {
        return load(page, size, null, null);
    }

    public AuthoritiesData load(final Integer page, final Integer size, final AuthoritiesSort sortBy, final SortDirection
            direction) {
        assertIsTemplated();
        loadSelf(expandPagedLink(selfLink, page, size, getSortByProperty(sortBy), direction));

        return new AuthoritiesData();
    }

    private void loadSelf(Link link) {
        String content = doGetForJSON(link.getHref());
        selfLink = readLink(link.getHref(), content, RestApiConstants.SELF, 1).get();
        searchLink = readLink(link.getHref(), content, RestApiConstants.SEARCH_RESOURCE, 1).get();
        nextLinkOptional = readLink(link.getHref(), content, RestApiConstants.NEXT_LINK, -1);
        prevLinkOptional = readLink(link.getHref(), content, RestApiConstants.PREV_LINK, -1);
    }

    private String getSortByProperty(AuthoritiesSort sortBy) {
        return sortBy == null ? null : sortBy.getProperty();
    }

    public final boolean hasPrev() {
        if (prevLinkOptional == null) {
            loadLinks();
        }
        return prevLinkOptional.isPresent();
    }

    public AuthoritiesResource prev(AuthoritiesSort sortBy, final SortDirection direction) {
        return nextPrev(hasPrev(), "prev", prevLinkOptional, sortBy, direction);
    }

    public AuthoritiesResource prev() {
        return prev(null, null);
    }

    public final boolean hasNext() {
        if (nextLinkOptional == null) {
            loadLinks();
        }
        return nextLinkOptional.isPresent();
    }

    public AuthoritiesResource next(AuthoritiesSort sortBy, final SortDirection direction) {
        return nextPrev(hasNext(), "next", nextLinkOptional, sortBy, direction);
    }

    public AuthoritiesResource next() {
        return next(null, null);
    }

    private AuthoritiesResource nextPrev(boolean availabilityCheckResult, String nextPrev, Optional<Link> link, final
    AuthoritiesSort sortBy, final SortDirection direction) {
        if (!availabilityCheckResult) {
            throw new IllegalStateException("no " + nextPrev + " page available");
        }
        if (link.get().isTemplated()) {
            return new AuthoritiesResource(expandPagedLink(link.get(), null, null, getSortByProperty(sortBy),
                    direction));
        }
        return new AuthoritiesResource(link.get());
    }

    public SearchResource search() {
        if (searchLink == null) {
            loadLinks();
        }
        return new SearchResource(searchLink);
    }

    private void assertIsTemplated() {
        if (!selfLink.isTemplated()) {
            throw new UnexpectedAPIFormatException("Resource \"{0}\" is not templated.", RestApiConstants.AUTHORITIES_RESOURCE);
        }
    }

    public class AuthoritiesData {
        public AuthoritiesResource getResource() {
            return AuthoritiesResource.this;
        }
    }
}
