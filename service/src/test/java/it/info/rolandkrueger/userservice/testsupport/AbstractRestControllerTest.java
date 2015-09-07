package it.info.rolandkrueger.userservice.testsupport;

import info.rolandkrueger.userservice.api.UserServiceAPI;
import info.rolandkrueger.userservice.api.model.AuthorityApiData;
import info.rolandkrueger.userservice.api.resources.AuthoritiesResource;
import info.rolandkrueger.userservice.api.resources.AuthorityResource;
import info.rolandkrueger.userservice.api.resources.UserService;
import info.rolandkrueger.userservice.api.resources.UsersResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.TestRestTemplate;

/**
 * @author Roland Krüger
 */
public abstract class AbstractRestControllerTest {

    private final static String CONTEXT_PATH = "http://localhost:8080";

    private static UserService service;

    static {
        service = UserServiceAPI.init(CONTEXT_PATH);
    }

    protected static UserService service() {
        return service;
    }

    protected static void deleteAllUsers() {
        UsersResource users;
        do {
            users = service.users();
            users.getData().stream().forEach(user -> user.getResource().delete());
        } while (users.hasNext());
    }

    protected static void deleteAllAuthorities() {
        AuthoritiesResource authorities;
        do {
            authorities = service.authorities();
            authorities.getData().stream().forEach(authority -> authority.getResource().delete());
        } while (authorities.hasNext());
    }

    protected static AuthorityApiData createAuthority(String authority) {
        AuthorityApiData authorityApiData = new AuthorityApiData();
        authorityApiData.setAuthority(authority);
        authorityApiData.setDescription("The " + authority + " role");
        return service().authorities().create(authorityApiData).getBody();
    }
}
