package info.rolandkrueger.userservice.model;

import info.rolandkrueger.userservice.api._internal.RestApiConstants;
import info.rolandkrueger.userservice.api.model.UserRegistrationApiData;
import info.rolandkrueger.userservice.controller.UserRegistrationRestController;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

/**
 * @author Roland Krüger
 */
public class UserRegistrationResource extends ResourceSupport {

    private String username;
    private String email;
    private String registrationConfirmationToken;

    protected UserRegistrationResource() {
    }

    public UserRegistrationResource(User user) {
        username = user.getUsername();
        email = user.getEmail();
        registrationConfirmationToken = user.getRegistrationConfirmationToken();

        add(ControllerLinkBuilder.linkTo(UserRegistrationRestController.class).slash(user
                .getRegistrationConfirmationToken()).slash(RestApiConstants.CONFIRM).withRel(RestApiConstants.CONFIRM));
    }

    public UserRegistrationResource(UserRegistrationApiData userRegistration) {
        this.username = userRegistration.getUsername();
        this.email = userRegistration.getEmail();
        this.registrationConfirmationToken = userRegistration.getRegistrationConfirmationToken();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRegistrationConfirmationToken() {
        return registrationConfirmationToken;
    }

    public void setRegistrationConfirmationToken(String registrationConfirmationToken) {
        this.registrationConfirmationToken = registrationConfirmationToken;
    }
}
