package it.info.rolandkrueger.userservice.controller;

import info.rolandkrueger.userservice.api.model.UserApiData;
import it.info.rolandkrueger.userservice.testsupport.AbstractRestControllerTest;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Roland Krüger
 */
public class UserRestControllerTest extends AbstractRestControllerTest {

    private UserApiData alice;

    @Before
    public void setUp() {
        deleteAllUsers();
        alice = registerUser("alice", "passw0rd", "alice@example.com");
    }

    @Test
    public void testFindUserForLogin_activeUser() {
        assertThat(findAliceForLogin().isPresent(), is(true));
    }

    @Test
    public void testFindUserForLogin_unconfirmedUser() {
        service()
                .userRegistrations()
                .create("bob", "passw0rd", "bob@example.com");

        Optional<UserApiData> bob = service()
                .users()
                .search()
                .findUserForLogin("bob").getData().stream().findFirst();
        assertThat(bob.isPresent(), is(false));
    }

    @Test
    public void testFindUserForLogin_disabledUser() {
        alice.setEnabled(false);
        alice.getResource().getUpdateResource().update();
        assertThatAliceCannotSignIn();
    }

    @Test
    public void testFindUserForLogin_accountLocked() {
        alice.setAccountNonLocked(false);
        alice.getResource().getUpdateResource().update();
        assertThatAliceCannotSignIn();
    }

    @Test
    public void testFindUserForLogin_accountExpired() {
        alice.setAccountNonExpired(false);
        alice.getResource().getUpdateResource().update();
        assertThatAliceCannotSignIn();
    }

    @Test
    public void testFindUserForLogin_credentialsExpired() {
        alice.setCredentialsNonExpired(false);
        alice.getResource().getUpdateResource().update();
        assertThatAliceCannotSignIn();
    }

    private void assertThatAliceCannotSignIn() {
        assertThat(findAliceForLogin().isPresent(), is(false));
    }

    private Optional<UserApiData> findAliceForLogin() {
        return service()
                .users()
                .search()
                .findUserForLogin("alice").getData().stream().findFirst();
    }
}
