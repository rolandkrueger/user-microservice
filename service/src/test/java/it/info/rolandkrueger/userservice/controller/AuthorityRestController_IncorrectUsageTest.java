package it.info.rolandkrueger.userservice.controller;

import info.rolandkrueger.userservice.api.model.AuthorityApiData;
import info.rolandkrueger.userservice.api.resources.AuthoritiesResource;
import it.info.rolandkrueger.userservice.testsupport.AbstractRestControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Roland Krüger
 */
public class AuthorityRestController_IncorrectUsageTest extends AbstractRestControllerTest {

    private AuthoritiesResource authoritiesResource;

    @Before
    public void setUp() {
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
