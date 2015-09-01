package info.rolandkrueger.userservice.api._internal;

import org.springframework.web.client.RestTemplate;

/**
 * @author Roland Krüger
 */
public class AbstractRestClient {

    protected RestTemplate restTemplate;

    protected AbstractRestClient() {
        restTemplate = new RestTemplate();
    }
}
