package info.rolandkrueger.userservice.controller;

import info.rolandkrueger.userservice.UserMicroserviceApplication;
import info.rolandkrueger.userservice.api.model.AuthorityApiData;
import info.rolandkrueger.userservice.api.resources.AuthoritiesResource;
import info.rolandkrueger.userservice.testsupport.AbstractRestControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Roland Krüger
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UserMicroserviceApplication.class)
@WebIntegrationTest(randomPort = true)
public class AuthorityRestController_IncorrectUsageTest extends AbstractRestControllerTest {

    private AuthoritiesResource authoritiesResource;

    @Value("${local.server.port}")
    private int port;

    @Before
    public void setUp() {
        setPort(port);
        deleteAllUsers();
        deleteAllAuthorities();
        authoritiesResource = service().authorities();
    }

    @Test
    public void createNullAuthority() {
        AuthorityApiData nullAuthority = new AuthorityApiData();
        try {
            authoritiesResource.create(nullAuthority);
        } catch (HttpClientErrorException exc) {
            assertThat(exc.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        }
    }

    @Test(expected = RestClientException.class)
    public void createAuthorityThatAlreadyExists() {
        createAuthority("admins");
        AuthorityApiData adminsAuthority = new AuthorityApiData();

        adminsAuthority.setAuthority("admins");
        authoritiesResource.create(adminsAuthority);
    }
}
