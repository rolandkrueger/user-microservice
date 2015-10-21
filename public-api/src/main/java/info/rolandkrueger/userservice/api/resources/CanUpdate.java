package info.rolandkrueger.userservice.api.resources;

import info.rolandkrueger.userservice.api._internal.model.AbstractBaseApiData;
import org.springframework.web.client.RestClientException;

/**
 * Interface to indicate that a paged resource ({@link info.rolandkrueger.userservice.api._internal.AbstractPagedResource})
 * supports updating existing entities via PUT.
 *
 * @param <T> the entity type which must be a subclass of {@link AbstractBaseApiData}
 * @author Roland Krüger
 */
public interface CanUpdate<T extends AbstractBaseApiData> {

    /**
     * Updates the given entity.
     *
     * @param entity entity to be updated
     * @throws RestClientException when an error has occurred while communicating with the service
     */
    void update(T entity) throws RestClientException;
}
