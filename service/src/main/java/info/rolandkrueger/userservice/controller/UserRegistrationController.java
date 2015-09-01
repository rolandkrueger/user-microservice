package info.rolandkrueger.userservice.controller;

import info.rolandkrueger.userservice.api._internal.RestApiConstants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Roland Krüger
 */
@RestController
@RequestMapping("/" + RestApiConstants.REGISTRATIONS_RESOURCE)
public class UserRegistrationController {

    @RequestMapping(method = RequestMethod.GET)
    public String getRegistration() {
        return "xxx";
    }

}
