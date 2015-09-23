package info.rolandkrueger.userservice.api.model;

import com.google.common.base.MoreObjects;
import info.rolandkrueger.userservice.api._internal.model.AbstractBaseApiData;
import info.rolandkrueger.userservice.api.resources.UserRegistrationsResource;
import org.springframework.hateoas.Link;

/**
 * @author Roland Krüger
 */
public class UserRegistrationApiData extends AbstractBaseApiData<UserRegistrationsResource> {
    private String forUsername;
    private String password;
    private String email;
    private String fullName;
    private String registrationConfirmationToken;

    public String getRegistrationConfirmationToken() {
        return registrationConfirmationToken;
    }

    public void setRegistrationConfirmationToken(String registrationConfirmationToken) {
        this.registrationConfirmationToken = registrationConfirmationToken;
    }

    public String getForUsername() {
        return forUsername;
    }

    public void setForUsername(String forUsername) {
        this.forUsername = forUsername.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    protected UserRegistrationsResource createNewResource(Link self) {
        return new UserRegistrationsResource(self);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(UserRegistrationApiData.class)
                .add("username", forUsername)
                .add("fullName", fullName)
                .add("email", email)
                .toString();
    }
}
