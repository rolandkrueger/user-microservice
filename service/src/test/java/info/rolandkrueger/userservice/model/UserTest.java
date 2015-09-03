package info.rolandkrueger.userservice.model;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Roland Krüger
 */
public class UserTest {

    private User user;

    @Before
    public void setUp() {
        user = new User("test");
        user.addAuthority(new Authority("user"));
    }

    @Test
    public void testConstructor() {
        assertThat(user.getRegistrationDate(), is(notNullValue()));
    }

    @Test
    public void testHasAuthority_with_string() {
        assertThat(user.hasAuthority("user"), is(true));
        assertThat(user.hasAuthority("admin"), is(false));
    }
    @Test
    public void testHasAuthority() {
        assertThat(user.hasAuthority(new Authority("user")), is(true));
        assertThat(user.hasAuthority(new Authority("admin")), is(false));
    }

    @Test
    public void testHasAuthority_withEmptyAuthorityList() {
        User user = new User("test");
        assertThat(user.hasAuthority("user"), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUsername_failsWithNull() {
        user.setUsername(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUsername_failsWithBlankString() {
        user.setUsername(" ");
    }

    @Test
    public void testUsernameIsTrimmed() {
        User user = new User("  user  ");
        assertThat(user.getUsername(), equalTo("user"));
    }

    @Test
    public void testPasswordEncryption() {
        user.setUnencryptedPassword("password");
        assertThat(user.getPassword(), is(notNullValue()));
        assertThat(user.getPassword(), not(equalTo("password")));
    }

    @Test
    public void testCreateRegistrationConfirmationToken() {
        final String registrationConfirmationToken = user.createRegistrationConfirmationToken();
        assertThat(user.getRegistrationConfirmationToken(), equalTo(registrationConfirmationToken));
    }

    @Test
    public void testClearRegistrationConfirmationToken() {
        user.createRegistrationConfirmationToken();
        assertThat(user.getRegistrationConfirmationToken(), is(notNullValue()));
        user.clearRegistrationConfirmationToken();
        assertThat(user.getRegistrationConfirmationToken(), is(nullValue()));
    }

    @Test
    public void testClearPassword() {
        user.setUnencryptedPassword("password");
        user.clearPassword();
        assertThat(user.getPassword(), is(nullValue()));
    }

    @Test
    public void testEquals() {
        User john = new User("john");
        User jane = new User("jane");

        assertThat(john, equalTo(john));
        assertThat(john, equalTo(new User("john")));

        assertThat(john, not(equalTo(jane)));
        assertThat(john, not(equalTo(null)));
    }

    @Test
    public void testHashCode() {
        User john = new User("john");
        User jane = new User("jane");

        assertThat(john.hashCode(), equalTo(john.hashCode()));
        assertThat(john.hashCode(), equalTo(new User("john").hashCode()));

        assertThat(john.hashCode(), not(equalTo(jane.hashCode())));
    }
}