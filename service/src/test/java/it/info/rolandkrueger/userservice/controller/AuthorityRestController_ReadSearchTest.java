package it.info.rolandkrueger.userservice.controller;

import info.rolandkrueger.userservice.api.model.AuthorityApiData;
import info.rolandkrueger.userservice.api.resources.AuthoritiesResource;
import info.rolandkrueger.userservice.api.resources.AuthoritiesSearchResource;
import it.info.rolandkrueger.userservice.testsupport.AbstractRestControllerTest;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;
import java.util.Optional;

import static it.info.rolandkrueger.userservice.testsupport.Asserts.authoritiesMatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/**
 * @author Roland Krüger
 */
public class AuthorityRestController_ReadSearchTest extends AbstractRestControllerTest {

    private static AuthorityApiData admins;
    private static AuthorityApiData users;
    private static AuthorityApiData editors;

    private static AuthoritiesResource authoritiesResource;
    private static AuthoritiesSearchResource searchResource;


    @BeforeClass
    public static void setUp() {
        deleteAllUsers();
        deleteAllAuthorities();

        admins = createAuthority("admins");
        users = createAuthority("users");
        editors = createAuthority("editors");
        authoritiesResource = service().authorities();
        searchResource = authoritiesResource.search();
    }

    @Test
    public void testGetAuthorityList() throws Exception {
        Collection<AuthorityApiData> authorityList = authoritiesResource.getData();
        assertThat(authorityList, hasSize(3));
    }

    @Test
    public void testFindByAuthority() throws Exception {
        Optional<AuthorityApiData> foundAuthorityOptional = searchResource.findByAuthority("admins")
                .getData()
                .stream().findFirst();
        assertThat("existing authority not found", foundAuthorityOptional.isPresent(), is(true));
        assertThat("found authority does not match expected authority object", authoritiesMatch(admins,
                foundAuthorityOptional.get()), is(true));

        foundAuthorityOptional = searchResource.findByAuthority("not_available")
                .getData()
                .stream().findFirst();
        assertThat("found authority object that is expected to be not available",
                foundAuthorityOptional.isPresent(), is(false));
    }

    @Test
    public void testFindByAuthority_isNullSafe() {
        Optional<AuthorityApiData> optional = searchResource.findByAuthority(null).getData().stream().findFirst();
        assertThat("unexpectedly found authority for null search", optional.isPresent(), is(false));
    }

    @Test
    public void testFindOneByAuthority() {
        Optional<AuthorityApiData> foundAuthority = searchResource.findOneByAuthority("users");
        assertThat("didn't find created authority", foundAuthority.isPresent(), is(true));
        assertThat("found authority doesn't match created authority",
                authoritiesMatch(users, foundAuthority.get()),
                is(true));

        foundAuthority = searchResource.findOneByAuthority("not_available");
        assertThat("found authority that is expected to not be available", foundAuthority.isPresent(), is(false));
    }


    @Test
    public void testFindOneByAuthority_isNullSafe() {
        Optional<AuthorityApiData> foundAuthority = searchResource.findOneByAuthority(null);
        assertThat("found authority that is expected to not be available", foundAuthority.isPresent(), is(false));
    }
}
