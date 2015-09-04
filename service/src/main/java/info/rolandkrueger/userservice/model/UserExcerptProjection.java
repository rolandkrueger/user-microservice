package info.rolandkrueger.userservice.model;

import info.rolandkrueger.userservice.api.enums.UserProjections;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Roland Krüger
 */
@Projection(name = UserProjections.Values.USER_EXCERPT_DATA, types = User.class)
public interface UserExcerptProjection {
    String getUsername();

    String getFullName();

    LocalDate getRegistrationDate();

    LocalDateTime getLastLogin();

    String getEmail();
}
