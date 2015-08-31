package it.info.rolandkrueger.userservice.controller;

import info.rolandkrueger.userservice.UserMicroserviceApplication;
import info.rolandkrueger.userservice.model.Authority;
import it.info.rolandkrueger.userservice.testsupport.AbstractRestControllerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static info.rolandkrueger.userservice.application.DevelopmentProfileConfiguration.*;
import static it.info.rolandkrueger.userservice.testsupport.Asserts.*;

/**
 * @author Roland Krüger
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { UserMicroserviceApplication.class })
@WebIntegrationTest(randomPort = true)
public class AuthorityRestControllerTest extends AbstractRestControllerTest {

    @Test
    public void testFindByAuthority() throws Exception {
        final ResponseEntity<Authority> response = restTemplate.getForEntity(toPath("/authority/admin"), Authority.class);
        assertEntityFound(response);
        assertAuthoritiesAreEqual(response.getBody(), admins);
    }

    @Test
    public void testFindByAuthority_NotFound() {
        assertEntityNotFound(restTemplate.getForEntity(toPath("/authority/invalid"), Authority.class));
    }

    @Test
    public void testGetAuthorityList() throws Exception {

    }

    @Test
    public void testCreate() throws Exception {

    }

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }
}