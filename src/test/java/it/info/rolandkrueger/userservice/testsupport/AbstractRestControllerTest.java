package it.info.rolandkrueger.userservice.testsupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * @author Roland Krüger
 */
public abstract class AbstractRestControllerTest {

    public final static String CONTEXT_PATH = "http://localhost:";

    @Value("${local.server.port}")
    protected int port;

    protected RestTemplate restTemplate;

    public AbstractRestControllerTest() {
        restTemplate = new TestRestTemplate();
    }

    protected String contextPath() {
        return CONTEXT_PATH + port;
    }

}
