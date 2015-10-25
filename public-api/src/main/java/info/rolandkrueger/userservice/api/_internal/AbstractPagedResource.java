package info.rolandkrueger.userservice.api._internal;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import info.rolandkrueger.userservice.api._internal.model.AbstractBaseApiData;
import info.rolandkrueger.userservice.api.enums.SortDirection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.util.Collection;
import java.util.HashMap;

/**
 * A REST collection resource that allows to page through the list of entities.
 *
 * @author Roland Krüger
 */
public abstract class AbstractPagedResource<T extends AbstractBaseApiData<?>, R extends AbstractPagedResource> extends AbstractResource<T> {

    private ResponseEntity<PagedResources<T>> responseEntity;

    protected AbstractPagedResource(Link templatedBaseLink, Link self) {
        super(templatedBaseLink, self);
    }

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

    public final boolean hasNext() throws RestClientException {
        loadIfNecessary();
        return responseEntity.getBody().getNextLink() != null;
    }

    public R next() throws RestClientException {
        R resource = createResourceInstance(nextPageLink());
        resource.setTemplatedBaseLink(templatedBaseLink);
        return resource;
    }

    private Link nextPageLink() throws RestClientException {
        Preconditions.checkState(hasNext(), "no next page available");
        loadIfNecessary();
        return responseEntity.getBody().getNextLink();
    }

    public final boolean hasPrevious() throws RestClientException {
        loadIfNecessary();
        return responseEntity.getBody().getPreviousLink() != null;
    }

    public R previous() throws RestClientException {
        R resource = createResourceInstance(previousPageLink());
        resource.setTemplatedBaseLink(templatedBaseLink);
        return resource;
    }

    private Link previousPageLink() throws RestClientException {
        Preconditions.checkState(hasPrevious(), "no previous page available");
        loadIfNecessary();
        return responseEntity.getBody().getPreviousLink();
    }

    public final Collection<T> getData() throws RestClientException {
        loadIfNecessary();
        return responseEntity.getBody().getContent();
    }

    /**
     * Provides the paging meta data for this paged resource which include the total number of elements an pages among others.
     *
     * @throws RestClientException when an error occurred while communicating with the service
     */
    public final PagedResources.PageMetadata getMetadata() throws RestClientException {
        loadIfNecessary();
        return responseEntity.getBody().getMetadata();
    }

    private void loadIfNecessary() throws RestClientException {
        if (responseEntity == null) {
            responseEntity = restTemplate.exchange(
                    self.expand().getHref(),
                    HttpMethod.GET,
                    entityForHALData,
                    getParameterizedTypeReferencePaged());
        }
    }

    protected ResponseEntity<? extends Resources<T>> getPagedResponseEntity() {
        loadIfNecessary();
        return responseEntity;
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
            if (size != null) {
                put("size", size);
            }
            if (sortBy != null) {
                put("sort", Joiner.on(',').skipNulls().join(sortBy,
                        MoreObjects.firstNonNull(direction, SortDirection.ASC).name()));
            }
        }});
    }

    /**
     * Delegate creation of a properly typed {@link ParameterizedTypeReference} instance to the subclass so that the
     * generic entity type T can be inferred by the RestTemplate.
     */
    protected abstract ParameterizedTypeReference<PagedResources<T>> getParameterizedTypeReferencePaged();

    /**
     * Reading single items is not supported for paged resources.
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public T read() {
        throw new UnsupportedOperationException();
    }
}
