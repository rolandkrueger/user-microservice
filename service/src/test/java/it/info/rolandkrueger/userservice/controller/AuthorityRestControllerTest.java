package it.info.rolandkrueger.userservice.controller;

import com.google.common.base.Preconditions;
import info.rolandkrueger.userservice.api.model.AuthorityApiData;
import info.rolandkrueger.userservice.api.resources.AuthoritiesResource;
import info.rolandkrueger.userservice.api.resources.AuthoritiesSearchResource;
import it.info.rolandkrueger.userservice.testsupport.AbstractRestControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Optional;

import static it.info.rolandkrueger.userservice.testsupport.Asserts.authoritiesMatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/**
 * @author Roland Krüger
 */
public class AuthorityRestControllerTest extends AbstractRestControllerTest {

    private AuthoritiesResource authoritiesResource;
    private AuthoritiesSearchResource searchResource;

    @Before
    public void setUp() {
        deleteAllUsers();
        deleteAllAuthorities();
        authoritiesResource = service().authorities();
        searchResource = authoritiesResource.search();
    }

    @Test
    public void testCreate() throws Exception {
        AuthorityApiData newAuthority = new AuthorityApiData();
        newAuthority.setAuthority("editors");
        newAuthority.setDescription("The editor role");
        ResponseEntity<AuthorityApiData> authorityResponseEntity = authoritiesResource.create(newAuthority);

        assertThat("list of currently available authorities has not exactly 1 element",
                authoritiesResource.getData(),
                hasSize(1));
        Optional<AuthorityApiData> editors = searchResource.findByAuthority("editors").getData().stream().findFirst();

        assertThat("found authority object does not match created authority",
                authoritiesMatch(newAuthority, editors.get()),
                is(true));
    }

    @Test
    public void testUpdate() throws Exception {
        AuthorityApiData editors = createAuthority("editors");

        AuthorityApiData loadedEditors = searchResource.findByAuthority("editors").getData().stream().findFirst().get();
        loadedEditors.setAuthority("EDITORS");
        loadedEditors.setDescription("The editor role - Updated");
        loadedEditors.getResource().update();

        AuthorityApiData updatedEditors = searchResource.findByAuthority("EDITORS").getData().stream().findFirst()
                .get();
        assertThat("update of authority resource data didn't work", authoritiesMatch(loadedEditors, updatedEditors), is
                (true));
    }

    @Test
    public void testDelete() throws Exception {
        createAuthority("users");
        assertThat(authoritiesResource.getData(), hasSize(1));
    }

}