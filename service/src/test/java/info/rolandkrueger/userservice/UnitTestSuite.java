package info.rolandkrueger.userservice;

import info.rolandkrueger.userservice.model.AuthorityTest;
import info.rolandkrueger.userservice.model.UserTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Roland Krüger
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(value = {AuthorityTest.class, UserTest.class})
public class UnitTestSuite {
}
