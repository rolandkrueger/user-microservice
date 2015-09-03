package info.rolandkrueger.userservice.api._internal;

import info.rolandkrueger.userservice.api.exceptions.UnexpectedAPIFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.hal.HalLinkDiscoverer;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Roland Krüger
 */
public abstract class AbstractRestClient {

    private Logger LOG = LoggerFactory.getLogger(getClass());

    protected final RestTemplate restTemplate;
    private final HttpEntity<String> entityForJSON;
    protected final HttpEntity<String> entityForHALData;

    protected AbstractRestClient() {
        restTemplate = new RestTemplate();
        HttpHeaders headersForJSON = new HttpHeaders();
        headersForJSON.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        entityForJSON = new HttpEntity<>(headersForJSON);

        HttpHeaders headersForHALData = new HttpHeaders();
        headersForHALData.setAccept(MediaType.parseMediaTypes("application/x-spring-data-verbose+json"));
        entityForHALData = new HttpEntity<>(headersForHALData);
    }

    protected Optional<Link> readLink(String targetURI, String content, String rel, int expectedLinks) {
        List<Link> links = new HalLinkDiscoverer().findLinksWithRel(rel, content);
        if (expectedLinks > -1 && links.size() != expectedLinks) {
            throw new UnexpectedAPIFormatException("Unexpected link structure at {2}: found {0} link(s) for " +
                    "resource \"{1}\" at {2} ({3} expected)", links.size(), rel, targetURI, expectedLinks);
        }

        return links.isEmpty() ? Optional.<Link>empty() : Optional.of(links.get(0));
    }

    protected String doGetForJSON(String targetURI) {
        LOG.debug("GET {}", targetURI);

        ResponseEntity<String> responseEntity = restTemplate.exchange(targetURI, HttpMethod.GET, entityForJSON, String.class);
        return responseEntity.getBody();
    }
}
