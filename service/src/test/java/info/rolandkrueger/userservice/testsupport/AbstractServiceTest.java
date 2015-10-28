package info.rolandkrueger.userservice.testsupport;

import info.rolandkrueger.userservice.model.Authority;
import info.rolandkrueger.userservice.model.User;
import info.rolandkrueger.userservice.service.AuthorityService;
import info.rolandkrueger.userservice.service.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author Roland Krüger
 */
@Transactional
public abstract class AbstractServiceTest {

    public Authority adminAuthority, userAuthority, developerAuthority;
    public User alice, bob, charly;

    public AbstractServiceTest() {
        adminAuthority = new Authority("admin", "The admin role");
        userAuthority = new Authority("user", "The user role");
        developerAuthority = new Authority("developer", "The developer role");
    }

    protected void createTestData(AuthorityService authorityService, UserService userService) {
        Arrays.asList(adminAuthority, userAuthority, developerAuthority)
                .stream()
                .forEach(authorityService::create);

        alice = new User("alice");
        alice.setUnencryptedPassword("alice");
        alice.createRegistrationConfirmationToken();
        alice.setEmail("alice@example.com");
        alice.addAuthority(adminAuthority);

        bob = new User("bob");
        bob.setUnencryptedPassword("bob");
        bob.addAuthority(developerAuthority);
        bob.addAuthority(userAuthority);

        charly = new User("charly");
        charly.setLastLogin(LocalDateTime.now());
        charly.setUnencryptedPassword("charly");

        Arrays.asList(alice, bob, charly)
                .stream()
                .forEach(userService::save);
    }
}
