package info.rolandkrueger.userservice.controller;

import info.rolandkrueger.userservice.UserMicroserviceApplication;
import info.rolandkrueger.userservice.model.User;
import info.rolandkrueger.userservice.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Roland Krüger
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UserMicroserviceApplication.class)
@Transactional
public class UserRestControllerTest {

    private static final String CONFIRMATION_TOKEN = "confirmation_token";

    @Autowired
    private UserRestController userRestController;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindUserByUsername() {
        final UserResponseEntity response = userRestController.findUserByUsername("alice");
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getUsername(), is("alice"));
        assertThat(response.getBody().getPassword(), is(nullValue()));
    }

    @Test
    public void testFindUserByUsername_NotFound() {
        final ResponseEntity<User> response = userRestController.findUserByUsername("invalid");
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void testFindUserByRegistrationToken() {
        createTestUser();
        final UserResponseEntity response = userRestController.findUserByRegistrationToken(CONFIRMATION_TOKEN);
        assertThat(response.getBody().getUsername(), is("test"));
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void testFindUserByRegistrationToken_NotFound() {
        final UserResponseEntity response = userRestController.findUserByRegistrationToken(CONFIRMATION_TOKEN);
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    private void createTestUser() {
        User test = new User("test");
        test.setUnencryptedPassword("test");
        ReflectionTestUtils.invokeSetterMethod(test, "setRegistrationConfirmationToken", CONFIRMATION_TOKEN);
        userRepository.save(test);
    }
}