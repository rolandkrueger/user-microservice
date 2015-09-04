package info.rolandkrueger.userservice.api.resources;

import info.rolandkrueger.userservice.api._internal.model.AbstractBaseApiData;
import org.springframework.web.client.RestClientException;

/**
 * @author Roland Krüger
 */
public interface CanDelete<T extends AbstractBaseApiData> {
    void delete(T entity) throws RestClientException;
}
