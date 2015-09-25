package info.rolandkrueger.userservice.api.model;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import info.rolandkrueger.userservice.api._internal.model.AbstractBaseApiData;
import info.rolandkrueger.userservice.api.resources.UserResource;
import org.springframework.hateoas.Link;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Roland Krüger
 */
public class UserApiData extends AbstractBaseApiData<UserResource> implements UserDetails {
    private String username;
    private String password;
    private String email;
    private boolean enabled;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private String rememberMeToken;
    private String registrationConfirmationToken;
    private LocalDate registrationDate;
    private LocalDateTime lastLogin;
    private List<AuthorityApiData> authorities;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public String getRememberMeToken() {
        return rememberMeToken;
    }

    public void setRememberMeToken(String rememberMeToken) {
        this.rememberMeToken = rememberMeToken;
    }

    public String getRegistrationConfirmationToken() {
        return registrationConfirmationToken;
    }

    public void setRegistrationConfirmationToken(String registrationConfirmationToken) {
        this.registrationConfirmationToken = registrationConfirmationToken;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public List<AuthorityApiData> getAuthorities() {
        if (authorities == null) {
            authorities = Lists.newArrayList();
        }
        return authorities;
    }

    public void setAuthorities(List<AuthorityApiData> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(UserApiData.class)
                .add("username", username)
                .toString();
    }

    @Override
    protected UserResource createNewResource(Link self) {
        return new UserResource(self, this);
    }
}
