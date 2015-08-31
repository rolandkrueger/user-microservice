package it.info.rolandkrueger.userservice.testsupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.TestRestTemplate;

/**
 * @author Roland Krüger
 */
public abstract class AbstractRestControllerTest {

    public final static String CONTEXT_PATH = "http://localhost:";

    @Value("${local.server.port}")
    protected int port;

    protected TestRestTemplate restTemplate;

    public AbstractRestControllerTest() {
        restTemplate = new TestRestTemplate();
    }

    protected String toPath(String uri) {
        return CONTEXT_PATH + port + uri;
    }

}
