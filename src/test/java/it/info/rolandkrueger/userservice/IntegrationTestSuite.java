package it.info.rolandkrueger.userservice;

import it.info.rolandkrueger.userservice.controller.UserRestControllerTest;
import it.info.rolandkrueger.userservice.repository.UserRepositoryTest;
import it.info.rolandkrueger.userservice.service.UserServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Roland Krüger
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(value = {
        UserRestControllerTest.class,
        UserRepositoryTest.class,
        UserServiceTest.class })
public class IntegrationTestSuite {
}
