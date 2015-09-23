package info.rolandkrueger.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import info.rolandkrueger.userservice.api._internal.RestApiConstants;
import info.rolandkrueger.userservice.api.model.UserRegistrationApiData;
import info.rolandkrueger.userservice.controller.UserRegistrationRestController;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

/**
 * @author Roland Krüger
 */
public class UserRegistrationResource extends ResourceSupport {

    private String forUsername;
    private String email;
    private String fullName;
    private String registrationConfirmationToken;

    protected UserRegistrationResource() {
    }

    public UserRegistrationResource(User user) {
        forUsername = user.getUsername();
        email = user.getEmail();
        fullName = user.getFullName();
        registrationConfirmationToken = user.getRegistrationConfirmationToken();

        add(ControllerLinkBuilder.linkTo(UserRegistrationRestController.class).slash(user
                .getRegistrationConfirmationToken()).slash(RestApiConstants.CONFIRM).withRel(RestApiConstants.CONFIRM));
    }

    public UserRegistrationResource(UserRegistrationApiData userRegistration) {
        this.forUsername = userRegistration.getForUsername();
        this.email = userRegistration.getEmail();
        this.fullName = userRegistration.getFullName();
        this.registrationConfirmationToken = userRegistration.getRegistrationConfirmationToken();
    }

    public String getForUsername() {
        return forUsername;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRegistrationConfirmationToken() {
        return registrationConfirmationToken;
    }

    public void setRegistrationConfirmationToken(String registrationConfirmationToken) {
        this.registrationConfirmationToken = registrationConfirmationToken;
    }
}
