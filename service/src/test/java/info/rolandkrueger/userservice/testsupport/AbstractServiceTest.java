package info.rolandkrueger.userservice.testsupport;

import info.rolandkrueger.userservice.model.Authority;
import info.rolandkrueger.userservice.model.User;
import info.rolandkrueger.userservice.service.AuthorityService;
import info.rolandkrueger.userservice.service.UserService;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author Roland Krüger
 */
public abstract class AbstractServiceTest {

    public Authority adminAuthority, userAuthority, developerAuthority;
    public User alice, bob, charly;

    public AbstractServiceTest() {
        adminAuthority = new Authority("admin", "The admin role");
        userAuthority = new Authority("user", "The user role");
        developerAuthority = new Authority("developer", "The developer role");
    }

    protected void createTestData(AuthorityService authorityService, UserService userService) {
        userService.getUserList(0, 100, Sort.Direction.ASC)
                .stream()
                .forEach(user -> userService.delete(user.getId()));

        authorityService.getAuthorityList(0, 100, Sort.Direction.ASC).stream().forEach(
                existingAuthority -> authorityService.delete(existingAuthority.getId()));

        Arrays.asList(adminAuthority, userAuthority, developerAuthority)
                .stream()
                .forEach(authority -> {
                    authorityService.create(authority);
                });

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
