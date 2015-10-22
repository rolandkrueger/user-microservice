package info.rolandkrueger.userservice.controller;

import info.rolandkrueger.userservice.UserMicroserviceApplication;
import info.rolandkrueger.userservice.api.model.UserApiData;
import info.rolandkrueger.userservice.api.model.UserRegistrationApiData;
import info.rolandkrueger.userservice.api.resources.UserRegistrationsResource;
import info.rolandkrueger.userservice.testsupport.AbstractRestControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;

/**
 * @author Roland Krüger
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UserMicroserviceApplication.class)
@WebIntegrationTest(randomPort = true)
public class UserRegistrationRestControllerTest extends AbstractRestControllerTest {

    @Value("${local.server.port}")
    private int port;

    @Before
    public void setUp() {
        setPort(port);
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
        assertThat(registrationData.getUsername(), is("test"));
    }

    @Test
    public void testSearchRegistration_invalidToken() {
        UserRegistrationsResource.UserRegistrationSearchResultResource registrationSearchResultResource =
                service()
                        .userRegistrations()
                        .findByToken("invalid");

        assertThat(registrationSearchResultResource.exists(), is(false));
    }

    @Test
    public void testConfirmRegistration() {
        ResponseEntity<UserRegistrationApiData> registrationResponse = executeTestRegistration();
        UserRegistrationsResource.UserRegistrationSearchResultResource registrationSearchResultResource =
                service()
                        .userRegistrations()
                        .findByToken(registrationResponse.getBody().getRegistrationConfirmationToken());

        assertThat(registrationSearchResultResource.exists(), is(true));
        ResponseEntity confirmationResponse = registrationSearchResultResource.confirmRegistration();
        assertThat(confirmationResponse.getStatusCode(), is(HttpStatus.OK));

        Optional<UserApiData> registeredUserOptional = service().users().search().findByUsername("test").getData().stream().findFirst();
        assertThat(registeredUserOptional.isPresent(), is(true));

        UserApiData registeredUser = registeredUserOptional.get();
        assertThat(registeredUser.getRegistrationConfirmationToken(), is(nullValue()));
        assertThat(registeredUser.isEnabled(), is(true));
    }

    private ResponseEntity<UserRegistrationApiData> executeTestRegistration() {
        UserRegistrationApiData registration = createTestRegistration();
        return service().userRegistrations().create(registration);
    }

    private UserRegistrationApiData createTestRegistration() {
        UserRegistrationApiData registration = new UserRegistrationApiData();
        registration.setUsername("test");
        registration.setEmail("test@example.com");
        registration.setPassword("passw0rd");
        return registration;
    }
}
