package it.info.rolandkrueger.userservice.controller;

import info.rolandkrueger.userservice.UserMicroserviceApplication;
import info.rolandkrueger.userservice.api.model.UserApiData;
import it.info.rolandkrueger.userservice.testsupport.AbstractRestControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Roland Krüger
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UserMicroserviceApplication.class)
@WebIntegrationTest(randomPort = true)
public class UserLoginRestControllerTest extends AbstractRestControllerTest {

    @Before
    public void setUp() {
        deleteAllUsers();
    }

    @Test
    public void testLoginUser() {
        registerUser("alice", "passw0rd", "alice@example.com");
        UserApiData alice = service().users().search().findUserForLogin("alice").getData().stream().findFirst().get();
        ResponseEntity loginResult = alice.getResource().loggedIn();
        assertThat(loginResult.getStatusCode(), is(HttpStatus.OK));

        UserApiData loggedInAlice = service().users().search().findByUsername("alice").getData().stream().findFirst().get();
        assertThat(loggedInAlice.getLastLogin(), is(notNullValue()));
    }

    @Test(expected = IllegalStateException.class)
    public void testLoginInactiveUser() {
        UserApiData alice = registerUser("alice", "passw0rd", "alice@example.com");
        alice.setEnabled(false);
        alice.getResource().getUpdateResource().update();

        alice.getResource().loggedIn();
    }
}
