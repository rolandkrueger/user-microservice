package info.rolandkrueger.userservice.api.resources;

import info.rolandkrueger.userservice.api._internal.model.BaseApiData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

/**
 * @author Roland Krüger
 */
public interface CanCreate<T extends BaseApiData> {
    ResponseEntity<T> create(T entity) throws RestClientException;
}
