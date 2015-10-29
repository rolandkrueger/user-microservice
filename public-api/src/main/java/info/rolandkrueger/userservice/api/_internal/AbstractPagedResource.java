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
 * {@link AbstractResource} interface supporting paging and sorting the collection resource's data.
 * <p/>
 * When you create a sub-class for this paged resource then parameter T denotes the data type represented by the resource,
 * and parameter R is the subclass itself. E. g., if you have a domain type <code>Person</code> and a corresponding
 * client-side data type <code>PersonApiData</code> then a paged <code>Person</code> paged resource could have the following
 * signature:
 * <p/>
 * <code>
 * public class PersonsResource extends AbstractPagedResource&lt;PersonApiData, PersonPagedResource&gt; {...}
 * </code>
 *
 * @param <T> type of the data object represented by this collection resource
 * @param <R> type of the sub-class inheriting from this resource
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
    protected AbstractPagedResource(Link templatedSelfLink, Link self) {
        super(templatedSelfLink, self);
    }

    /**
     * Factory method for creating a new instance of this resource. Has to be implemented by sub-classes in such a way
     * that this implementation returns a new instance of the implementing class for the given self link.
     *
     * @param self link to self to be used by the created resource object
     * @return a new instance of the implementing sub-class
     */
    protected abstract R createResourceInstance(Link self);

    /**
     * Sets the templated link to self for this resource.
     */
    protected final void setTemplatedSelfLink(Link templatedSelfLink) {
        this.templatedSelfLink = templatedSelfLink;
    }

    /**
     * Configures this resource to return its data as a sorted list for a given page with a given size. This method
     * returns a new paged resource object with its self link parameterized to sort by the specified property and to
     * return the specified page. This method will only create a parameterized resource object and will not yet call
     * the service to fetch the data.
     *
     * @param page           requested page number
     * @param size           requested page size
     * @param sortByProperty sort property, has to be a valid property name of the resource's data type
     * @param direction      sort direction: ascending or descending
     * @return a new paged resource object with its self link parameterized to sort by the specified parameters limited by
     * the specified page
     */
    public final R goToPageSorted(Integer page, Integer size, String sortByProperty, SortDirection direction) {
        R resource = createResourceInstance(expandLink(templatedSelfLink, page, size, sortByProperty, direction));
        resource.setTemplatedSelfLink(templatedSelfLink);
        return resource;
    }

    /**
     * Configures this resource to return its data as a sorted list. This method returns a new paged resource object
     * with its self link parameterized to sort by the specified property. This method will only create a parameterized
     * resource object and will not yet call the service to fetch the data.
     * <p/>
     * Note that this method will retain any paging parameters already set with {@link #goToPage(Integer, Integer)}.
     * I. e., the following two statements are equivalent:
     * <code>
     * pagedResource.goToPage(5, 20).sort("name", SortDirection.ASC);
     * pageResource.goToPageSorted(5, 20, "name", SortDirection.ASC);
     * </code>
     *
     * @param sortByProperty sort property, has to be a valid property name of the resource's data type
     * @param direction      sort direction: ascending or descending
     * @return a new paged resource object with its self link parameterized to sort by the specified parameters
     */
    public final R sort(String sortByProperty, SortDirection direction) {
        R resource = createResourceInstance(expandLink(templatedSelfLink, null, null, sortByProperty, direction));
        resource.setTemplatedSelfLink(templatedSelfLink);
        return resource;
    }

    /**
     * Configures this resource to go to a particular page on the resource data list.
     * <p/>
     * Note that this method will retain any sorting parameters already set with {@link #sort(String, SortDirection)}.
     * I. e., the following two statements are equivalent:
     * <code>
     * pagedResource.goToPage(5, 20).sort("name", SortDirection.ASC);
     * pageResource.goToPageSorted(5, 20, "name", SortDirection.ASC);
     * </code>
     *
     * @param page requested page number
     * @param size requested page size
     * @return a new paged resource object with its self link parameterized to provide a particular page
     */
    public final R goToPage(Integer page, Integer size) {
        R resource = createResourceInstance(expandLink(templatedSelfLink, page, size, null, null));
        resource.setTemplatedSelfLink(templatedSelfLink);
        return resource;
    }

    /**
     * Checks if there is a next page available for the current page.
     * <p/>
     * This method will call the service (if not already done) to read this information from the service response.
     *
     * @return <code>true</code> if {@link #next()} can be called
     * @throws RestClientException when an error occurred while communicating with the service
     */
    public final boolean hasNext() throws RestClientException {
        loadIfNecessary();
        return responseEntity.getBody().getNextLink() != null;
    }

    /**
     * Returns the next page for the list of resource data objects. Prior to calling this method, it should be
     * checked with {@link #hasNext()} if there is a next page available at all.
     *
     * @return a new paged resource object with its page parameters configured to provide the next page
     * @throws RestClientException   when an error occurred while communicating with the service
     * @throws IllegalStateException if there is no next page available
     */
    public final R next() throws RestClientException {
        R resource = createResourceInstance(nextPageLink());
        resource.setTemplatedSelfLink(templatedSelfLink);
        return resource;
    }

    /**
     * Returns the link to the next page of this paged resource.
     *
     * @throws RestClientException   when an error occurred while communicating with the service
     * @throws IllegalStateException if this resource points to the last page of the collection resource
     */
    private Link nextPageLink() throws RestClientException {
        Preconditions.checkState(hasNext(), "no next page available");
        loadIfNecessary();
        return responseEntity.getBody().getNextLink();
    }

    /**
     * Checks if there is a previous page available for the current page.
     * <p/>
     * This method will call the service (if not already done) to read this information from the service response.
     *
     * @return <code>true</code> if {@link #previous()} can be called
     * @throws RestClientException when an error occurred while communicating with the service
     */
    public final boolean hasPrevious() throws RestClientException {
        loadIfNecessary();
        return responseEntity.getBody().getPreviousLink() != null;
    }

    /**
     * Returns the previous page for the list of resource data objects. Prior to calling this method, it should be
     * checked with {@link #hasPrevious()} if there is a previous page available at all.
     *
     * @return a new paged resource object with its page parameters configured to provide the previous page
     * @throws RestClientException   when an error occurred while communicating with the service
     * @throws IllegalStateException if there is no previous page available
     */
    public final R previous() throws RestClientException {
        R resource = createResourceInstance(previousPageLink());
        resource.setTemplatedSelfLink(templatedSelfLink);
        return resource;
    }

    /**
     * Returns the link to the previous page of this paged resource.
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
     * @return an expanded link object whose template parameters are configured with the given values. This resulting
     * link may be templated itself.
     * @throws IllegalArgumentException if the provided link is not templated
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
