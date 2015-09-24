package info.rolandkrueger.userservice.service;

import info.rolandkrueger.userservice.UserMicroserviceApplication;
import info.rolandkrueger.userservice.model.User;
import it.info.rolandkrueger.userservice.testsupport.AbstractServiceTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static it.info.rolandkrueger.userservice.testsupport.Asserts.assertUsersAreEqual;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Roland Krüger
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UserMicroserviceApplication.class)
@Transactional
public class UserServiceTest extends AbstractServiceTest {

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() {
        createTestData(authorityService, userService);
    }

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