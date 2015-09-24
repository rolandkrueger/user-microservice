package it.info.rolandkrueger.userservice.service;

import info.rolandkrueger.userservice.UserMicroserviceApplication;
import info.rolandkrueger.userservice.api.service.StaticEndpointProvider;
import info.rolandkrueger.userservice.api.service.UserDetailServiceImpl;
import info.rolandkrueger.userservice.service.exception.UserNotFoundException;
import it.info.rolandkrueger.userservice.testsupport.AbstractRestControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Roland Krüger
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UserMicroserviceApplication.class)
@WebIntegrationTest(randomPort = true)
public class UserDetailServiceImplTest extends AbstractRestControllerTest {

    private UserDetailServiceImpl userDetailService;
    private StaticEndpointProvider endpointProvider;

    @Value("${local.server.port}")
    private int port;

    @Before
    public void setUp() {
        setPort(port);
        deleteAllUsers();
        endpointProvider = new StaticEndpointProvider();
        endpointProvider.setEndpoint("http://localhost:" + port);

        userDetailService = new UserDetailServiceImpl(endpointProvider);
    }

    @Test
    public void testLoadUser_success() {
        registerUser("alice", "passw0rd", "alice@example.com");
        UserDetails alice = userDetailService.loadUserByUsername("alice");
        assertThat(alice.getPassword(), is(notNullValue()));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUser_invalidUser() {
        userDetailService.loadUserByUsername("invalid");
    }
}
