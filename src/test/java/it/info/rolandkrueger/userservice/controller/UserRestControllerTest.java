package it.info.rolandkrueger.userservice.controller;

import java.time.LocalDate;

import info.rolandkrueger.userservice.UserMicroserviceApplication;
import info.rolandkrueger.userservice.model.User;
import info.rolandkrueger.userservice.service.UserService;
import it.info.rolandkrueger.userservice.testsupport.AbstractRestControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static info.rolandkrueger.userservice.application.DevelopmentProfileConfiguration.*;
import static it.info.rolandkrueger.userservice.testsupport.Asserts.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;
import static org.mockito.Mockito.*;

/**
 * @author Roland Krüger
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { UserMicroserviceApplication.class })
@WebIntegrationTest(randomPort = true)
public class UserRestControllerTest extends AbstractRestControllerTest {

    @Mock
    private UserService userServiceMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindUserByUsername() throws Exception {
        User roland = new User("roland");
        when(userServiceMock.findUserByUsername("roland")).thenReturn(roland);

        final ResponseEntity<User> userResponseEntity = restTemplate.getForEntity(toPath("/user/roland"), User.class);

        assertThat(userResponseEntity.getStatusCode(), is(HttpStatus.OK));
        User user = userResponseEntity.getBody();
        assertThat(user.getUsername(), is("roland"));
        assertThat(user.getRegistrationDate(), is(LocalDate.now()));
    }

    @Test
    public void testFindUserByUsername_NotFound() throws Exception {
        final ResponseEntity<User> response = restTemplate.getForEntity(toPath("/user/tyrion"), User.class);
        assertEntityNotFound(response);
    }

    @Test
    public void testFindUserByRegistrationToken() {
        when(userServiceMock.findByRegistrationConfirmationToken("valid_confirmation_token")).thenReturn(alice);

        final ResponseEntity<User> response = restTemplate.getForEntity(toPath("/user?token=valid_confirmation_token"), User.class);
        assertEntityFound(response);
        assertUsersAreEqual(response.getBody(), alice);
    }

    @Test
    public void testFindUserByRegistrationToken_NotFound() {
        when(userServiceMock.findByRegistrationConfirmationToken("unknown_token")).thenReturn(null);
        assertEntityNotFound(restTemplate.getForEntity(toPath("/user?token=invalid_token"), User.class));
    }
}