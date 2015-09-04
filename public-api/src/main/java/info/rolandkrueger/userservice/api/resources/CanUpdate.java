package info.rolandkrueger.userservice.api.resources;

import info.rolandkrueger.userservice.api._internal.model.AbstractBaseApiData;
import org.springframework.web.client.RestClientException;

/**
 * @author Roland Krüger
 */
public interface CanUpdate<T extends AbstractBaseApiData> {
    void update(T entity) throws RestClientException;
}
