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
 * A REST collection resource that allows to page through the list of entities. This class adds methods to the general
 * {@link AbstractResource} interface which support paging and sorting the collection resource's data.
 *
 * @param <T> type of the data object represented by this collection resource
 * @author Roland Krüger
 * @see AbstractBaseApiData
 */
public abstract class AbstractPagedResource<T extends AbstractBaseApiData<?>, R extends AbstractPagedResource> extends AbstractResource<T> {

    /**
     * Response entity object for this collection resource.
     */
    private ResponseEntity<PagedResources<T>> responseEntity;

    /**
     * {@inheritDoc}
     */
    protected AbstractPagedResource(Link templatedBaseLink, Link self) {
        super(templatedBaseLink, self);
    }

    protected abstract R createResourceInstance(Link self);

    protected final void setTemplatedBaseLink(Link templatedBaseLink) {
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

    public final R goToPage(Integer page, Integer size) {
        R resource = createResourceInstance(expandLink(templatedBaseLink, page, size, null, null));
        resource.setTemplatedBaseLink(templatedBaseLink);
        return resource;
    }

    public final boolean hasNext() throws RestClientException {
        loadIfNecessary();
        return responseEntity.getBody().getNextLink() != null;
    }

    public final R next() throws RestClientException {
        R resource = createResourceInstance(nextPageLink());
        resource.setTemplatedBaseLink(templatedBaseLink);
        return resource;
    }

    /**
     * Returns the link to the next page of this paged resource. Before this method is called, it should be checked
     * whether there is a next page available with {@link #hasNext()}.
     *
     * @throws RestClientException   when an error occurred while communicating with the service
     * @throws IllegalStateException if this resource points to the last page of the collection resource
     */
    private Link nextPageLink() throws RestClientException {
        Preconditions.checkState(hasNext(), "no next page available");
        loadIfNecessary();
        return responseEntity.getBody().getNextLink();
    }

    public final boolean hasPrevious() throws RestClientException {
        loadIfNecessary();
        return responseEntity.getBody().getPreviousLink() != null;
    }

    public final R previous() throws RestClientException {
        R resource = createResourceInstance(previousPageLink());
        resource.setTemplatedBaseLink(templatedBaseLink);
        return resource;
    }

    /**
     * Returns the link to the previous page of this paged resource. Before this method is called, it should be checked
     * whether there is a previous page available with {@link #hasPrevious()}.
     *
     * @throws RestClientException   when an error occurred while communicating with the service
     * @throws IllegalStateException if this resource points to the first page of the collection resource
     */
    private Link previousPageLink() throws RestClientException {
        Preconditions.checkState(hasPrevious(), "no previous page available");
        loadIfNecessary();
        return responseEntity.getBody().getPreviousLink();
    }

    /**
     * Returns the resource entity data for the page currently selected by the paging parameters. The data will be
     * loaded lazily.
     *
     * @return a collection of data objects loaded from the currently selected page of the resource
     * @throws RestClientException when an error occurred while communicating with the service
     */
    public final Collection<T> getData() throws RestClientException {
        loadIfNecessary();
        return responseEntity.getBody().getContent();
    }

    /**
     * Provides the paging meta data for this paged resource which include the total number of elements an pages
     * among others.
     *
     * @throws RestClientException when an error occurred while communicating with the service
     */
    public final PagedResources.PageMetadata getMetadata() throws RestClientException {
        loadIfNecessary();
        return responseEntity.getBody().getMetadata();
    }

    /**
     * Loads this resource if not already done. This method will check whether the response entity field is
     * <code>null</code> and if so, it will perform a GET request on the service. The target of this GET request
     * is this resource's self link. This method is needed to support lazy loading of resource data.
     *
     * @throws RestClientException if an error occurred during communicating with the server
     * @see #self
     */
    private void loadIfNecessary() throws RestClientException {
        if (responseEntity == null) {
            responseEntity = restTemplate.exchange(
                    self.expand().getHref(),
                    HttpMethod.GET,
                    entityForHALData,
                    getParameterizedTypeReferencePaged());
        }
    }

    /**
     * Returns the paged response entity for this resource. Requests this entity from the server if the entity object
     * is <code>null</code>.
     */
    protected final ResponseEntity<? extends Resources<T>> getPagedResponseEntity() {
        loadIfNecessary();
        return responseEntity;
    }

    /**
     * Expands the templated link given as first parameter with the values for the page, the page size, a sort property,
     * and a sort direction. If any of the parameters is <code>null</code>, a default value will be used. This default
     * value is defined by the service's configuration.
     * <p/>
     * Page indices are zero-based, i.e. the first 25 entries can be fetched with page = 0 and size = 25.
     *
     * @param link      link to be expanded; needs to be a templated link which contains parameters page, size, sortBy
     *                  and direction
     * @param page      page number (may be <code>null</code>)
     * @param size      size of the requested page (may be <code>null</code>)
     * @param sortBy    sort property (may be <code>null</code>)
     * @param direction sort direction (ascending or descending; may be <code>null</code> and will be ignored is sortBy
     *                  is <code>null</code>). If <code>null</code>, ascending sort direction will be used by default.
     * @return
     */
    protected final Link expandLink(final Link link, final Integer page, final Integer size, final String sortBy, final
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
    public final T read() {
        throw new UnsupportedOperationException();
    }
}
