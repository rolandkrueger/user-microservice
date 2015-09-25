package info.rolandkrueger.userservice.service;

import info.rolandkrueger.userservice.api.model.UserApiData;
import info.rolandkrueger.userservice.api.service.AuthenticationListener;
import info.rolandkrueger.userservice.api.service.StaticEndpointProvider;
import info.rolandkrueger.userservice.testsupport.AbstractRestControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Roland Krüger
 */
public class AuthenticationListenerTest extends AbstractRestControllerTest {

    private AuthenticationListener listener;

    @Before
    public void setUp() {
        deleteAllUsers();
        deleteAllAuthorities();
        listener = new AuthenticationListener(new StaticEndpointProvider("http://localhost:8080"));
        registerUser("alice", "passw0rd", "alice@example.com");
    }

    @Test
    public void testAuthenticationSuccess() {
        UserApiData user = new UserApiData();
        user.setUsername("alice");

        listener.onApplicationEvent(new InteractiveAuthenticationSuccessEvent(
                new UsernamePasswordAuthenticationToken(user, ""), getClass()));

        UserApiData alice = service().users().search().findByUsername("alice").getData().stream().findFirst().get();
        assertThat(alice.getLastLogin(), is(notNullValue()));
    }

    @Test
    public void testAuthenticationSuccessForInactiveUser() {
        UserApiData alice = service().users().search().findByUsername("alice").getData().stream().findFirst().get();
        alice.setEnabled(false);
        alice.getResource().getUpdateResource().update();

        listener.onApplicationEvent(new InteractiveAuthenticationSuccessEvent(
                new UsernamePasswordAuthenticationToken(alice, ""), getClass()));

        alice = service().users().search().findByUsername("alice").getData().stream().findFirst().get();
        assertThat(alice.getLastLogin(), is(nullValue()));
    }

}
