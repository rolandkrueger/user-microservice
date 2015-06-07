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

import java.util.Arrays;

/**
 * @author Roland Krüger
 */
@Component
@Profile("DEV")
public class DevelopmentProfileConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    private final static Logger LOG = LoggerFactory.getLogger(DevelopmentProfileConfiguration.class);

    private Authority admins, users, developers;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createAuthorities();
        createUsers();
    }

    private void createAuthorities() {
        LOG.info("Creating test data: authorities 'admin', 'user', 'developer'");

        admins = new Authority("admin");
        users = new Authority("user");
        developers = new Authority("developer");

        authorityRepository.save(Arrays.asList(admins, users, developers));
    }

    private void createUsers() {
        LOG.info("Creating test data: users 'alice', 'bob', 'charly'");

        User alice = new User("alice");
        alice.setUnencryptedPassword("alice");
        alice.createRegistrationConfirmationToken();
        alice.setEmail("alice@example.com");
        alice.addAuthority(admins);

        User bob = new User("bob");
        bob.setUnencryptedPassword("bob");
        bob.setFullName("Bob");
        bob.addAuthority(developers);
        bob.addAuthority(users);

        User charly = new User("charly");
        charly.setUnencryptedPassword("charly");

        userRepository.save(Arrays.asList(alice, bob, charly));
    }
}
