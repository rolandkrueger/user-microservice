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
public abstract class AbstractPagedResource<T, R extends AbstractPagedResource> extends AbstractRestClient {

    private Link templatedBaseLink;
    protected Link self;
    private ResponseEntity<PagedResources<T>> responseEntity;

    protected AbstractPagedResource(Link templatedBaseLink, Link self) {
        Preconditions.checkNotNull(self);
        Preconditions.checkNotNull(templatedBaseLink);

        this.templatedBaseLink = templatedBaseLink;
        this.self = self;
    }

    /**
     * Delegate creation of a properly typed {@link ParameterizedTypeReference} instance to the subclass so that the
     * generic entity type T can be inferred by the RestTemplate.
     */
    protected abstract ParameterizedTypeReference<PagedResources<T>> getParameterizedTypeReference();

    protected abstract R createResourceInstance(Link self);

    protected void setTemplatedBaseLink(Link templatedBaseLink) {
        this.templatedBaseLink = templatedBaseLink;
    }

    public final R goToPageSorted(Integer page, Integer size, String sortBy, SortDirection direction) {
        R resource = createResourceInstance(expandLink(templatedBaseLink, page, size, sortBy, direction));
        resource.setTemplatedBaseLink(templatedBaseLink);
        return resource;
    }

    public final R sort(String sortBy, SortDirection direction) {
        R resource = createResourceInstance(expandLink(templatedBaseLink, null, null, sortBy, direction));
        resource.setTemplatedBaseLink(templatedBaseLink);
        return resource;
    }

    public R goToPage(Integer page, Integer size) {
        R resource = createResourceInstance(expandLink(templatedBaseLink, page, size, null, null));
        resource.setTemplatedBaseLink(templatedBaseLink);
        return resource;
    }

    public final boolean hasNext() {
        loadIfNecessary();
        return responseEntity.getBody().getNextLink() != null;
    }

    public R next() {
        R resource = createResourceInstance(nextPageLink());
        resource.setTemplatedBaseLink(templatedBaseLink);
        return resource;
    }

    private Link nextPageLink() {
        Preconditions.checkState(hasNext(), "no next page available");
        loadIfNecessary();
        return responseEntity.getBody().getNextLink();
    }

    public final boolean hasPrevious() {
        loadIfNecessary();
        return responseEntity.getBody().getPreviousLink() != null;
    }

    public R previous() {
        R resource = createResourceInstance(previousPageLink());
        resource.setTemplatedBaseLink(templatedBaseLink);
        return resource;
    }

    private Link previousPageLink() {
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
                self.expand().getHref(),
                HttpMethod.GET,
                entityForHALData,
                getParameterizedTypeReference());
    }

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
