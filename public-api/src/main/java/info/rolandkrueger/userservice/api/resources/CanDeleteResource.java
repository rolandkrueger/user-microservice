package info.rolandkrueger.userservice.api.resources;

import org.springframework.web.client.RestClientException;

/**
 * @author Roland Krüger
 */
public interface CanDeleteResource {
    void delete() throws RestClientException;
}
