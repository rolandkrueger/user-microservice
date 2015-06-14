package it.info.rolandkrueger.userservice.service;

import info.rolandkrueger.userservice.UserMicroserviceApplication;
import info.rolandkrueger.userservice.model.User;
import info.rolandkrueger.userservice.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static info.rolandkrueger.userservice.application.DevelopmentProfileConfiguration.*;
import static it.info.rolandkrueger.userservice.testsupport.Asserts.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author Roland Krüger
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UserMicroserviceApplication.class)
@Transactional
@IntegrationTest("server.port:0")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testFindByRegistrationConfirmationToken() {
        final User alice = userService.findUserByUsername("alice");
        final String confirmationToken = alice.getRegistrationConfirmationToken();

        final User foundUser = userService.findByRegistrationConfirmationToken(confirmationToken);
        assertUsersAreEqual(foundUser, alice);
    }

    @Test
    public void testFindByRegistrationConfirmationToken_NotFound() {
        assertThat(userService.findByRegistrationConfirmationToken("invalid"), is(nullValue()));
    }

    @Test
    public void testFindUserByUsername_NotFound() {
        assertThat(userService.findUserByUsername("invalid"), is(nullValue()));
    }

    @Test
    public void testFindUserByUsername() {
        assertUsersAreEqual(userService.findUserByUsername("alice"), alice);
    }
}