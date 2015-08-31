package info.rolandkrueger.userservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Roland Krüger
 */
@RestController
@RequestMapping("/registrations")
public class UserRegistrationController {

    @RequestMapping(method = RequestMethod.GET)
    public String getRegistration() {
        return "xxx";
    }

}
