package info.rolandkrueger.userservice.api._internal;

import com.google.common.base.Preconditions;
import info.rolandkrueger.userservice.api._internal.model.AbstractBaseApiData;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.Optional;

/**
 * Client-side representation of a REST resource. This class will be used to deserialize the JSON+HAL server responses
 * returned for calls to the RESTful API. A resource contains the actual domain data as its payload and, in addition,
 * contextual links to related resources. Such a resource link for any given relationship type can be obtained with
 * {@link #getLinkFor(ResponseEntity, String)}. Based on these links new {@link AbstractResource} instances can be
 * created.
 *
 * @author Roland Krüger
 */
public abstract class AbstractResource<T extends AbstractBaseApiData<?>> extends AbstractRestClient {

    /**
     * Link to self: this is the target URL for this resource. GETting this link will fetch this resource's data including
     * all references to other resources which are available in the current context. This link is not templated.
     *
     * @see Link#isTemplated()
     */
    protected Link self;

    /**
     * Templated link to self: this is the templated target URL for this resource. A templated link can contain additional
     * parameters that first need to be specified and resolved to obtain a non-templated link which can then be used
     * to GET the resource's data.
     */
    protected Link templatedSelfLink;

    /**
     * Response entity that is available as soon as a service call has been performed on the server.
     */
    private ResponseEntity<T> responseEntity;

    /**
     * The resource data that was fetched from the service. This is the actual domain data payload which is provided
     * by the service along with contextual resource links.
     */
    private T data;

    /**
     * Creates a new resource instance to the specified non-templated self link and with the data object predefined with
     * the given entity. This constructor is typically use in cases where a new entity shall be created or an existing
     * one updated on the server.
     *
     * @param selfLink self link to the resource
     * @param data     data entity to be managed by this resource
     */
    protected AbstractResource(Link selfLink, T data) {
        this(selfLink);
        this.data = data;
    }

    /**
     * Constructor that uses the given templated link both as templated link and as self link. The given link should
     * not be templated.
     *
     * @param selfLink link to the resource
     */
    public AbstractResource(Link selfLink) {
        this(selfLink, selfLink);
    }

    /**
     * Create a new resource object with two {@link Link} objects pointing to the endpoint for the referenced
     * resource on the server. The first link may be templated, while the second link object has to be the (non-templated)
     * link via which this resource was originally accessed. I.e., this non-templated link is typically generated when
     * traversing the (parameterized) linked resources of the service.
     *
     * @param templatedSelfLink link to this resource which may be templated.
     * @param self              link to this resource. It is expected that this link is not templated.
     * @see Link#isTemplated()
     */
    public AbstractResource(Link templatedSelfLink, Link self) {
        Preconditions.checkNotNull(self);
        Preconditions.checkNotNull(templatedSelfLink);
        this.self = self;
        this.templatedSelfLink = templatedSelfLink;
    }

    /**
     * Returns an object of class {@link ParameterizedTypeReference} which is needed by the
     * {@link org.springframework.web.client.RestTemplate} in order to interpret the JSON+HAL response from the service.
     * <p/>
     * This method is typically implemented by sub-classes by creating and returning a new empty object of class
     * {@link ParameterizedTypeReference}
     */
    protected abstract ParameterizedTypeReference<T> getParameterizedTypeReference();

    /**
     * Returns the {@link Class} object of the resource type <code>T</code>.
     */
    protected abstract Class<T> getResourceType();

    /**
     * Returns the data object belonging to this resource.
     *
     * @see AbstractBaseApiData
     */
    protected final T getApiData() {
        return data;
    }

    /**
     * Provides the link for a given projection. This link is derived from the specified target link by expanding this
     * link with the {@link RestApiConstants#PROJECTION} parameter. The target link has to be templated.
     *
     * @param targetLink link to be expanded with the {@link RestApiConstants#PROJECTION} parameter
     * @param projection name of the projection to be used
     * @return the target link expanded with the given projection name
     */
    protected final Link getProjectionLink(Link targetLink, String projection) {
        return targetLink.expand(Collections.singletonMap(RestApiConstants.PROJECTION, projection));
    }

    /**
     * Lazily provides the response entity for this resource. If the response entity has not yet been loaded it will
     * be loaded lazily by this method.
     *
     * @return the response entity for this resource which contains possible error codes
     * @throws RestClientException when an error occurred while communicating with the service
     * @see #loadIfNecessary()
     */
    protected final ResponseEntity<T> getResponseEntity() throws RestClientException {
        loadIfNecessary();
        return responseEntity;
    }

    /**
     * Loads this resource if not already done. This method will check whether the response entity field is
     * <code>null</code> and if so, it will perform a GET request on the service. The target of this GET request
     * is this resource's self link. This method is needed to support lazy loading of resource data.
     *
     * @throws RestClientException if an error occurred during communicating with the server
     * @see #self
     */
    private void loadIfNecessary() {
        if (responseEntity == null) {
            responseEntity = restTemplate.exchange(
                    self.expand().getHref(),
                    HttpMethod.GET,
                    entityForHALData,
                    getParameterizedTypeReference());
        }
    }

    /**
     * Retrieves a link for any given relationship (<code>rel</code>) from the specified response entity. If the
     * specified relationship is not defined in the response entity's link section, <code>null</code> is returned by
     * this method.
     *
     * @param responseEntity the response entity from which the link should be retrieved
     * @param rel            the requested relationship
     * @return the requested link or <code>null</code> if no such link could be found
     */
    protected final Link getLinkFor(ResponseEntity<? extends AbstractBaseApiData<?>> responseEntity, String rel) {
        Optional<Link> linkOptional = responseEntity.getBody().getLinks()
                .stream()
                .filter(link -> link.getRel().equals(rel))
                .findAny();

        return linkOptional.orElse(null);
    }

    /**
     * Creates the given entity at the server using a HTTP POST operation. The result of this POST operation is returned
     * as an instance of {@link ResponseEntity}. The operation's success is indicated by the response entity's status
     * code. If this status code is {@link HttpStatus#CREATED} the entity has successfully been created. In that case
     * the entity is returned by the server and can be obtained from the response entity.
     *
     * @param entity the entity to be created
     * @return a response entity containing the result of the operation
     * @throws RestClientException when an error occurred while communicating with the service
     * @see ResponseEntity#getStatusCode()
     */
    protected final ResponseEntity<T> createInternal(T entity) throws RestClientException {
        return restTemplate.exchange(
                self.expand().getHref(),
                HttpMethod.POST,
                new HttpEntity<>(entity, HEADERS_FOR_HAL_DATA),
                getParameterizedTypeReference());
    }

    /**
     * Reads the data object from this resource and returns it. Loads the data lazily.
     *
     * @throws RestClientException when an error occurred while communicating with the service
     */
    public T read() throws RestClientException {
        return getResponseEntity().getBody();
    }

    /**
     * Update the given entity on the server using a HTTP PUT operation. Uses the self link as target for the PUT
     * operation.
     * <p/>
     * Can be used by resources implementing the {@link info.rolandkrueger.userservice.api.resources.CanUpdateResource}
     * or the {@link info.rolandkrueger.userservice.api.resources.CanUpdate} interface.
     *
     * @param entity the data entity to be updated on the server
     * @throws RestClientException when an error occurred while communicating with the service
     */
    protected final void updateInternal(T entity) throws RestClientException {
        restTemplate.put(entity.getSelf().expand().getHref(), entity);
    }

    /**
     * Update the given entity at the specified link on the server using a HTTP PUT operation. Uses the given link
     * as target for the PUT operation.
     * <p/>
     * Can be used by resources implementing the {@link info.rolandkrueger.userservice.api.resources.CanUpdateResource}
     * or the {@link info.rolandkrueger.userservice.api.resources.CanUpdate} interface.
     *
     * @param entity the data entity to be updated on the server
     * @param target target link to be used for PUTting the given entity
     * @throws RestClientException when an error occurred while communicating with the service
     */
    protected final void updateInternal(Link target, T entity) throws RestClientException {
        restTemplate.put(target.expand().getHref(), entity);
    }

    /**
     * Deletes the given entity on the server using a HTTP DELETE operation. Uses the self link as target for the DELETE
     * operation.
     *
     * @param entity the entity to be deleted
     * @throws RestClientException when an error occurred while communicating with the service
     */
    protected final void deleteInternal(T entity) throws RestClientException {
        restTemplate.delete(entity.getSelf().expand().getHref());
    }
}
