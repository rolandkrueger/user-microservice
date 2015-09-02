package it.info.rolandkrueger.userservice.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.rolandkrueger.userservice.UserMicroserviceApplication;
import info.rolandkrueger.userservice.api.*;
import info.rolandkrueger.userservice.model.Authority;
import info.rolandkrueger.userservice.model.User;
import it.info.rolandkrueger.userservice.testsupport.AbstractRestControllerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.rest.webmvc.ResourceType;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.hateoas.mvc.TypeReferences.ResourcesType;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
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
        ResponseEntity<String> userEntity = restTemplate.getForEntity(toPath("users/search/findByUsername?username=bob"), String.class);
        String body = userEntity.getBody();
    }

    @Test
    public void test2() {
        UserService userService = UserServiceAPI.init("http://localhost:8080");

        AuthoritiesResource authorities = userService.authorities(0, 1);
        System.out.println("Page 1: " + authorities.getData());

        authorities = authorities.next();
        System.out.println("Page 2: " + authorities.getData());
    }

    @Test
    public void test() {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(MediaType.parseMediaTypes("application/x-spring-data-verbose+json"));
        HttpEntity<String> entity = new HttpEntity<>(headers);

//        ResponseEntity<PagedResources<User>> exchange = restTemplate.exchange
//                ("http://localhost:8080/users", HttpMethod.GET, entity, new TypeReferences.PagedResourcesType<User>());

        ResponseEntity<PagedResources<User>> exchange = restTemplate.exchange
                ("http://localhost:8080/users?page=0&size=1", HttpMethod.GET, entity, new
                        ParameterizedTypeReference<PagedResources<User>>() {
                        });

        System.out.println(exchange.getBody().getNextLink());
        System.out.println(exchange.getBody().getPreviousLink());

        System.out.println(exchange.getBody().getContent());

    }

}
