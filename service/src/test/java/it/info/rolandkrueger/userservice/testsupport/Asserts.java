package it.info.rolandkrueger.userservice.testsupport;

import com.google.common.base.Preconditions;
import info.rolandkrueger.userservice.api.model.AuthorityApiData;
import info.rolandkrueger.userservice.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.Is.is;

/**
 * @author Roland Krüger
 */
public class Asserts {
    public static void assertEntityNotFound(ResponseEntity<?> response) {
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    public static void assertEntityFound(ResponseEntity<?> response) {
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    public static void assertUsersAreEqual(User actual, User expected) {
        assertThat(actual, is(expected));
        assertThat(actual.getEmail(), is(expected.getEmail()));
        assertThat(actual.getAuthorities(), containsInAnyOrder(expected.getAuthorities().toArray()));
        assertThat(actual.getRegistrationConfirmationToken(), is(expected.getRegistrationConfirmationToken()));
    }

    public static boolean authoritiesMatch(AuthorityApiData expected, AuthorityApiData actual) {
        Preconditions.checkNotNull(expected);
        Preconditions.checkNotNull(actual);
        return expected.getAuthority().equals(actual.getAuthority()) && expected.getDescription().equals(actual.getDescription());
    }
}
