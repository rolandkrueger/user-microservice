package info.rolandkrueger.userservice.api._internal;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * Base class for the REST client classes that each represent one individual resource on the server. Provides two
 * objects with protected visibility to sub-classes: an instance of {@link RestTemplate} to be used to access the
 * RESTful API, and an instance of {@link HttpEntity} that has the media type header preset to
 * "application/x-spring-data-verbose+json". The latter is needed to receive the data from the API in such a way
 * that it can be interpreted by the client.
 *
 * @author Roland Krüger
 */
public abstract class AbstractRestClient {

    /**
     * {@link RestTemplate} for getting access to the service.
     */
    protected final RestTemplate restTemplate;

    /**
     * A pre-configured {@link HttpEntity} object with its media type header set to "application/x-spring-data-verbose+json".
     */
    protected final HttpEntity<String> entityForHALData;
    private final static HttpHeaders HEADERS_FOR_HAL_DATA;

    static {
        HEADERS_FOR_HAL_DATA = new HttpHeaders();
        HEADERS_FOR_HAL_DATA.setAccept(MediaType.parseMediaTypes("application/x-spring-data-verbose+json"));
    }

    protected AbstractRestClient() {
        restTemplate = new RestTemplate();
        entityForHALData = new HttpEntity<>(HEADERS_FOR_HAL_DATA);
    }
}
