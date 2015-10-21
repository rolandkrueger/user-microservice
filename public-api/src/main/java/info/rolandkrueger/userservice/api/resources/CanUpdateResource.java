package info.rolandkrueger.userservice.api.resources;

import org.springframework.web.client.RestClientException;

/**
 * Interface to indicate that a resource supports updating existing entities via PUT.
 *
 * @author Roland Krüger
 */
public interface CanUpdateResource {

    /**
     * Updates this resource.
     *
     * @throws RestClientException when an error has occurred while communicating with the service
     */
    void update() throws RestClientException;
}
