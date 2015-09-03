package info.rolandkrueger.userservice.api._internal;

import info.rolandkrueger.userservice.api.exceptions.UnexpectedAPIFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.hal.HalLinkDiscoverer;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

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
