package info.rolandkrueger.userservice.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.rest.core.config.Projection;

/**
 * @author Roland Krüger
 */
@Projection(name = "no-authorities-no-pw", types = User.class)
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
