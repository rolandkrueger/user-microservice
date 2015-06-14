package it.info.rolandkrueger.userservice.testsupport;

import info.rolandkrueger.userservice.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.*;

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
        assertThat(actual.getFullName(), is(expected.getFullName()));
        assertThat(actual.getEmail(), is(expected.getEmail()));
        assertThat(actual.getAuthorities(), containsInAnyOrder(expected.getAuthorities().toArray()));
        assertThat(actual.getRegistrationConfirmationToken(), is(expected.getRegistrationConfirmationToken()));
    }
}
