package info.rolandkrueger.userservice.api.resources;

import info.rolandkrueger.userservice.api._internal.model.AbstractBaseApiData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

/**
 * Interface to indicate that a resource supports creating new entities via POST.
 *
 * @param <T> the entity type which must be a subclass of {@link AbstractBaseApiData}
 * @author Roland Krüger
 */
public interface CanCreate<T extends AbstractBaseApiData> {

    /**
     * Creates a new entity of the given type.
     *
     * @param entity entity to be created by the service
     * @return a {@link ResponseEntity} containing the created entity as returned by the service and the HTTP status code
     * of the operation
     * @throws RestClientException when an error has occurred while communicating with the service
     */
    ResponseEntity<T> create(T entity) throws RestClientException;
}
