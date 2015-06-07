package it.info.rolandkrueger.userservice;

import info.rolandkrueger.userservice.controller.UserRestControllerTest;
import it.info.rolandkrueger.userservice.repository.UserRepositoryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Roland Krüger
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(value = {UserMicroserviceApplicationTests.class,
        UserRepositoryTest.class, UserRestControllerTest.class})
public class IntegrationTestSuite {
}
