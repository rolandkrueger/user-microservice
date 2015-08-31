package it.info.rolandkrueger.userservice.controller;

import info.rolandkrueger.userservice.UserMicroserviceApplication;
import info.rolandkrueger.userservice.model.User;
import it.info.rolandkrueger.userservice.testsupport.AbstractRestControllerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

import static java.util.Collections.singletonMap;

/**
 * @author Roland Krüger
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = { UserMicroserviceApplication.class })
//@WebIntegrationTest(randomPort = true)
public class UserControllerTest extends AbstractRestControllerTest {

    @Test
    public void testUpdateUserData() {
        ResponseEntity<String> userEntity =restTemplate.getForEntity(toPath("users/search/findByUsername?username=bob"), String.class);
        String body = userEntity.getBody();
    }
}
