package it.info.rolandkrueger.userservice.controller;

import info.rolandkrueger.userservice.api.enums.UserProjections;
import info.rolandkrueger.userservice.api.model.AuthorityApiData;
import info.rolandkrueger.userservice.api.model.UserApiData;
import it.info.rolandkrueger.userservice.testsupport.AbstractRestControllerTest;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;

/**
 * @author Roland Krüger
 */
public class UpdateUserRestControllerTest extends AbstractRestControllerTest {

    private UserApiData alice;
    private String alicePasswordHash;

    @Before
    public void setUp() {
        deleteAllUsers();
        deleteAllAuthorities();

        createAuthority("admin");
        createAuthority("user");
        createAuthority("editor");

        alice = registerUser("alice", "passw0rd", "alice@example.com");
        alicePasswordHash = service()
                .users()
                .search().findByUsername("alice")
                .getData().stream().findFirst().get()
                .getResource()
                .useProjection(UserProjections.FULL_DATA)
                .read()
                .getPassword();
    }

    @Test
    public void testUpdateUser() throws Exception {
        alice.setUsername("bob");
        alice.setEmail("new@email.com");
        alice.setAccountNonExpired(false);
        alice.setAccountNonLocked(false);
        alice.setCredentialsNonExpired(false);
        alice.setEnabled(false);
        alice.setRememberMeToken("rememberme");
        alice.setPassword("newpassword");
        alice.getAuthorities().add(new AuthorityApiData("admin", ""));

        alice.setRegistrationConfirmationToken("ignored");
        alice.setRegistrationDate(LocalDate.MIN);
        alice.setLastLogin(LocalDateTime.MIN);

        alice.getResource().getUpdateResource().update();

        alice = service()
                .users()
                .search()
                .findByUsername("bob")
                .getData().stream().findFirst().get()
                .getResource()
                .useProjection(UserProjections.FULL_DATA)
                .read();

        assertThat(alice.getUsername(), is("bob"));
        assertThat(alice.getEmail(), is("new@email.com"));
        assertThat(alice.isAccountNonExpired(), is(false));
        assertThat(alice.isAccountNonLocked(), is(false));
        assertThat(alice.isCredentialsNonExpired(), is(false));
        assertThat(alice.isEnabled(), is(false));
        assertThat(alice.getRememberMeToken(), is("rememberme"));
        assertThat(alice.getAuthorities(), hasSize(1));
        assertThat(alice.getPassword(), is(not(alicePasswordHash)));
        assertThat(alice.getPassword(), is(notNullValue()));

        assertThat(alice.getRegistrationConfirmationToken(), is(nullValue()));
        assertThat(alice.getRegistrationDate().getYear(), is(LocalDate.now().getYear()));
        assertThat(alice.getLastLogin(), is(nullValue()));
    }

    @Test
    public void testAddAuthorities() throws Exception {
        alice.getResource().getUpdateResource().addAuthorities("admin");
        reloadAlice();
        assertThat(alice.getAuthorities(), hasSize(1));

        alice.getResource().getUpdateResource().addAuthorities("user", "editor");
        reloadAlice();
        assertThat(alice.getAuthorities(), hasSize(3));
    }

    @Test
    public void testUnknownAuthoritiesAreIgnored() {
        alice.getResource().getUpdateResource().addAuthorities("admin", "invalid", "unknown");
        reloadAlice();
        assertThat(alice.getAuthorities(), hasSize(1));
    }

    @Test
    public void testRemoveAuthorities() throws Exception {
        alice.getResource().getUpdateResource().addAuthorities("admin", "user", "editor");
        alice.getResource().getUpdateResource().removeAuthorities("admin", "editor", "invalid");
        reloadAlice();
        assertThat(alice.getAuthorities(), hasSize(1));
    }

    @Test
    public void testClearAuthorities() throws Exception {
        alice.getResource().getUpdateResource().addAuthorities("admin", "user", "editor");
        reloadAlice();
        assertThat(alice.getAuthorities(), hasSize(3));
        alice.getResource().getUpdateResource().clearAuthorities();
        reloadAlice();
        assertThat(alice.getAuthorities(), hasSize(0));
    }

    private void reloadAlice() {
        alice = service()
                .users()
                .search()
                .findByUsername("alice")
                .getData().stream().findFirst().get()
                .getResource()
                .useProjection(UserProjections.FULL_DATA)
                .read();
    }

}