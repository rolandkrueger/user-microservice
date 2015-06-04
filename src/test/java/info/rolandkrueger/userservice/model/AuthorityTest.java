package info.rolandkrueger.userservice.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * @author Roland Krüger
 */
public class AuthorityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_failsWithNull() throws Exception {
        new Authority(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_failsWithBlankString() throws Exception {
        new Authority(" ");
    }

    @Test
    public void testEquals() {
        Authority userAuthority = new Authority("user");
        Authority adminAuthority = new Authority("admin");

        assertThat(userAuthority, not(equalTo(adminAuthority)));
        assertThat(userAuthority, equalTo(new Authority("user")));
        assertThat(userAuthority, equalTo(userAuthority));

        assertThat(userAuthority, not(equalTo(null)));
    }

    @Test
    public void testHashCode() {
        Authority userAuthority = new Authority("user");
        Authority adminAuthority = new Authority("admin");

        assertThat(userAuthority.hashCode(), not(equalTo(adminAuthority.hashCode())));
        assertThat(userAuthority.hashCode(), equalTo(new Authority("user").hashCode()));
    }
}