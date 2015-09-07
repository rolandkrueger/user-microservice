package it.info.rolandkrueger.userservice.controller;

import it.info.rolandkrueger.userservice.testsupport.AbstractRestControllerTest;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Roland Krüger
 */
public class UserControllerTest extends AbstractRestControllerTest {


    @Before
    public void setUp() {
        deleteAllUsers();
    }

    @Test
    public void test() {
    }

}
