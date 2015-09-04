package info.rolandkrueger.userservice.api.resources;

import info.rolandkrueger.userservice.api._internal.model.BaseApiData;
import org.springframework.web.client.RestClientException;

/**
 * @author Roland Krüger
 */
public interface CanUpdate<T extends BaseApiData> {
    void update(T entity) throws RestClientException;
}
