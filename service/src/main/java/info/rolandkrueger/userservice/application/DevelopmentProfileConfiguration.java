package info.rolandkrueger.userservice.application;

import info.rolandkrueger.userservice.model.Authority;
import info.rolandkrueger.userservice.model.User;
import info.rolandkrueger.userservice.repository.AuthorityRepository;
import info.rolandkrueger.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author Roland Krüger
 */
@Component
@Profile("DEV")
public class DevelopmentProfileConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    private final static Logger LOG = LoggerFactory.getLogger(DevelopmentProfileConfiguration.class);

    public final static Authority admins, users, developers;
    public final static User alice, bob, charly;

    static {
        LOG.info("Creating test data: authorities 'admin', 'user', 'developer'");

        admins = new Authority("admin", "The admin role");
        users = new Authority("user", "The user role");
        developers = new Authority("developer", "The developer role");

        LOG.info("Creating test data: users 'alice', 'bob', 'charly'");
        alice = new User("alice");
        alice.setUnencryptedPassword("alice");
        alice.createRegistrationConfirmationToken();
        alice.setEmail("alice@example.com");
        alice.addAuthority(admins);

        bob = new User("bob");
        bob.setUnencryptedPassword("bob");
        bob.setFullName("Bob");
        bob.addAuthority(developers);
        bob.addAuthority(users);

        charly = new User("charly");
        charly.setLastLogin(LocalDateTime.now());
        charly.setUnencryptedPassword("charly");
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        saveAuthorities();
        saveUsers();
    }

    private void saveAuthorities() {
        authorityRepository.save(Arrays.asList(admins, users, developers));
    }

    private void saveUsers() {
        final List<User> users = Arrays.asList(alice, bob, charly);
        userRepository.save(users);
        LOG.info("Added users {}", users);
    }
}
