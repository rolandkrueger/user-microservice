package info.rolandkrueger.userservice.api._internal;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * @author Roland Krüger
 */
public abstract class AbstractRestClient {

    protected final RestTemplate restTemplate;
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
