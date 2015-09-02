package info.rolandkrueger.userservice.api._internal;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import info.rolandkrueger.userservice.api.SortDirection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.HashMap;

/**
 * @author Roland Krüger
 */
public abstract class AbstractPagedResource<T> extends AbstractRestClient {

    protected Link self;
    protected boolean isAtPage;
    private ResponseEntity<PagedResources<T>> responseEntity;

    protected AbstractPagedResource(Link self, boolean isAtPage) {
        Preconditions.checkNotNull(self);
        this.isAtPage = isAtPage;
        this.self = self;
    }

    protected final Link goToPageSorted(Integer page, Integer size, String sortBy, SortDirection direction) {
        if (page != null && isAtPage) {
            throw new IllegalStateException("cannot go to page: page parameter is already set");
        }
        return expandLink(self, page, size, sortBy, direction);
    }

    protected final Link sort(String sortBy, SortDirection direction) {
        return goToPageSorted(null, null, sortBy, direction);
    }

    protected final Link getLinkForPage(Integer page, Integer size) {
        return goToPageSorted(page, size, null, null);
    }

    public final boolean hasNext() {
        loadIfNecessary();
        return responseEntity.getBody().getNextLink() != null;
    }

    protected final Link nextPageLink() {
        Preconditions.checkState(hasNext(), "no next page available");
        loadIfNecessary();
        return responseEntity.getBody().getNextLink();
    }

    public final boolean hasPrevious() {
        loadIfNecessary();
        return responseEntity.getBody().getPreviousLink() != null;
    }

    protected final Link previousPageLink() {
        Preconditions.checkState(hasPrevious(), "no previous page available");
        loadIfNecessary();
        return responseEntity.getBody().getPreviousLink();
    }

    public final Collection<T> getData() {
        loadIfNecessary();
        return responseEntity.getBody().getContent();
    }

    public final PagedResources.PageMetadata getMetadata() {
        loadIfNecessary();
        return responseEntity.getBody().getMetadata();
    }

    private void loadIfNecessary() {
        if (responseEntity != null) {
            return;
        }

        responseEntity = restTemplate.exchange(
                self.getHref(),
                HttpMethod.GET,
                entityForHALData,
                getParameterizedTypeReference());
    }

    /**
     * Delegate creation of a properly typed {@link ParameterizedTypeReference} instance to the subclass so that the
     * generic entity type T can be inferred by the RestTemplate.
     */
    protected abstract ParameterizedTypeReference<PagedResources<T>> getParameterizedTypeReference();

    protected Link expandLink(final Link link, final Integer page, final Integer size, final String sortBy, final
    SortDirection direction) {
        if (!link.isTemplated()) {
            throw new IllegalArgumentException("cannot expand link: link is not templated");
        }

        return link.expand(new HashMap<String, Object>(3) {{
            if (page != null) {
                put("page", page);
            }
            if (page != null) {
                put("size", size);
            }
            if (sortBy != null) {
                put("sort", Joiner.on(',').skipNulls().join(sortBy,
                        MoreObjects.firstNonNull(direction, SortDirection.ASC).name()));
            }
        }});
    }

}
