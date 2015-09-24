package it.info.rolandkrueger.userservice.testsupport;

import info.rolandkrueger.userservice.api.UserServiceAPI;
import info.rolandkrueger.userservice.api.model.AuthorityApiData;
import info.rolandkrueger.userservice.api.model.UserApiData;
import info.rolandkrueger.userservice.api.model.UserRegistrationApiData;
import info.rolandkrueger.userservice.api.resources.AuthoritiesResource;
import info.rolandkrueger.userservice.api.resources.AuthorityResource;
import info.rolandkrueger.userservice.api.resources.UserService;
import info.rolandkrueger.userservice.api.resources.UsersResource;
import org.junit.AfterClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @author Roland Krüger
 */
public abstract class AbstractRestControllerTest {

    private final static String CONTEXT_PATH = "http://localhost:8080";

    private static UserService service;

    protected static UserService service() {
        if (service == null) {
            service = UserServiceAPI.init(CONTEXT_PATH);
        }
        return service;
    }

    protected static void deleteAllUsers() {
        UsersResource users;
        do {
            users = service().users();
            users.getData().stream().forEach(user -> user.getResource().delete());
        } while (users.hasNext());
    }

    protected static void deleteAllAuthorities() {
        AuthoritiesResource authorities;
        do {
            authorities = service().authorities();
            authorities.getData().stream().forEach(authority -> authority.getResource().delete());
        } while (authorities.hasNext());
    }

    protected static AuthorityApiData createAuthority(String authority) {
        AuthorityApiData authorityApiData = new AuthorityApiData();
        authorityApiData.setAuthority(authority);
        authorityApiData.setDescription("The " + authority + " role");
        return service().authorities().create(authorityApiData).getBody();
    }

    protected static UserApiData registerUser(String username, String password, String email) {
        ResponseEntity<UserRegistrationApiData> registrationResponse =
                service()
                        .userRegistrations()
                        .create(username, password, email);

        service()
                .userRegistrations()
                .findByToken(registrationResponse.getBody().getRegistrationConfirmationToken())
                .confirmRegistration();

        return service()
                .users()
                .search()
                .findByUsername(username).getData().stream().findFirst().get();
    }

}
