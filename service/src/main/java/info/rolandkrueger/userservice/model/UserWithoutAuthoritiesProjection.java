package info.rolandkrueger.userservice.model;

import info.rolandkrueger.userservice.api.enums.UserProjections;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Roland Krüger
 */
@Projection(name = UserProjections.Values.NO_AUTHORITIES_NO_PW, types = User.class)
public interface UserWithoutAuthoritiesProjection {
    boolean isAccountNonExpired();

    boolean isAccountNonLocked();

    boolean isCredentialsNonExpired();

    boolean isEnabled();

    String getUsername();

    String getFullName();

    String getRememberMeToken();

    String getRegistrationConfirmationToken();

    LocalDate getRegistrationDate();

    LocalDateTime getLastLogin();

    String getEmail();
}
