package it.info.rolandkrueger.userservice.controller;

import info.rolandkrueger.userservice.api.model.UserRegistrationApiData;
import info.rolandkrueger.userservice.api.resources.UserRegistrationsResource;
import it.info.rolandkrueger.userservice.testsupport.AbstractRestControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import static junit.framework.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Roland Krüger
 */
public class UserRegistrationRestControllerTest extends AbstractRestControllerTest {

    @Before
    public void setUp() {
        deleteAllUsers();
    }

    @Test
    public void testRegistration() {
        ResponseEntity<UserRegistrationApiData> registrationResponse = executeTestRegistration();
        UserRegistrationApiData responseBody = registrationResponse.getBody();

        assertThat(registrationResponse.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(responseBody.getRegistrationConfirmationToken(), is(notNullValue()));
        assertThat(responseBody.getPassword(), is(nullValue()));
    }

    @Test
    public void testRegistration_failsForExistingUser() {
        UserRegistrationApiData registration = createTestRegistration();
        service().userRegistrations().create(registration);

        try {
            service().userRegistrations().create(registration);
        } catch (HttpStatusCodeException exc) {
            assertThat(exc.getStatusCode(), is(HttpStatus.CONFLICT));
            return;
        }
        fail("Conflict status code expected");
    }

    @Test
    public void testSearchRegistration() {
        ResponseEntity<UserRegistrationApiData> registrationResponse = executeTestRegistration();
        UserRegistrationsResource.UserRegistrationSearchResultResource registrationSearchResultResource =
                service()
                        .userRegistrations()
                        .findByToken(registrationResponse.getBody().getRegistrationConfirmationToken());

        UserRegistrationApiData registrationData = registrationSearchResultResource.read();

        assertThat(registrationData, is(notNullValue()));
        assertThat(registrationData.getForUsername(), is("test"));
    }

    @Test
    public void testSearchRegistration_invalidToken() {
        UserRegistrationsResource.UserRegistrationSearchResultResource registrationSearchResultResource =
                service()
                        .userRegistrations()
                        .findByToken("invalid");

        try {
            registrationSearchResultResource.read();
        } catch (HttpStatusCodeException exc) {
            assertThat(exc.getStatusCode(), is(HttpStatus.NOT_FOUND));
            return;
        }
        fail("404 Not Found expected");
    }

    private ResponseEntity<UserRegistrationApiData> executeTestRegistration() {
        UserRegistrationApiData registration = createTestRegistration();
        return service().userRegistrations().create(registration);
    }

    private UserRegistrationApiData createTestRegistration() {
        UserRegistrationApiData registration = new UserRegistrationApiData();
        registration.setForUsername("test");
        registration.setEmail("test@example.com");
        registration.setFullName("Test test");
        registration.setPassword("passw0rd");
        return registration;
    }
}
