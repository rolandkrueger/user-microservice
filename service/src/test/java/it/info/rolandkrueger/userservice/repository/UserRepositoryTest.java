package it.info.rolandkrueger.userservice.repository;

import javax.validation.ConstraintViolationException;

import info.rolandkrueger.userservice.UserMicroserviceApplication;
import info.rolandkrueger.userservice.model.User;
import info.rolandkrueger.userservice.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Roland Krüger
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UserMicroserviceApplication.class)
@Transactional
public class UserRepositoryTest {

    private User user;

    @Before
    public void setUp() {
        user = new User("user");
        user.setUnencryptedPassword("password");
        user.setEmail("user@example.com");
        user.setFullName("John Doe");
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    public void test_save_and_load_user() {
        userRepository.save(user);
        final User loadedUser = userRepository.findByUsername("user");
        assertThat(loadedUser, is(equalTo(user)));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void test_username_unique_in_db() {
        User user1 = new User("user");
        user1.setUnencryptedPassword("password");
        User user2 = new User("user");
        user2.setUnencryptedPassword("password");
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test(expected = ConstraintViolationException.class)
    public void test_save_user_with_empty_email() {
        user.setEmail(" ");
        userRepository.save(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void test_save_user_with_invalid_email() {
        user.setEmail("invalid email address");
        userRepository.save(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void test_save_user_with_null_registration_date() {
        user.setRegistrationDate(null);
        userRepository.save(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void test_save_user_with_null_password() {
        user = new User("user");
        user.setEmail("user@example.com");
        userRepository.save(user);
    }

    @Test
    public void test_load_nonexisting_user() {
        final User user = userRepository.findByUsername("invalid");
        assertThat(user, is(nullValue()));
    }

    @Test
    public void test_findByRegistrationConfirmationToken() {
        final String registrationConfirmationToken = user.createRegistrationConfirmationToken();
        userRepository.save(user);
        final User foundUser = userRepository.findByRegistrationConfirmationToken(registrationConfirmationToken);
        assertThat(foundUser, equalTo(user));
    }
}
