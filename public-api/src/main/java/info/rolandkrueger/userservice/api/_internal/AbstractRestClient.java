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

    private Logger LOG = LoggerFactory.getLogger(getClass());

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

    protected Optional<Link> readLink(String targetURI, String content, String rel, int expectedLinks) {
        List<Link> links = new HalLinkDiscoverer().findLinksWithRel(rel, content);
        if (expectedLinks > -1 && links.size() != expectedLinks) {
            throw new UnexpectedAPIFormatException("Unexpected link structure at {2}: found {0} link(s) for " +
                    "resource \"{1}\" at {2} ({3} expected)", links.size(), rel, targetURI, expectedLinks);
        }

        return links.isEmpty() ? Optional.<Link>empty() : Optional.of(links.get(0));
    }

}
