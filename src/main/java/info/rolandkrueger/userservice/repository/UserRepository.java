package info.rolandkrueger.userservice.repository;

import info.rolandkrueger.userservice.model.User;
import info.rolandkrueger.userservice.model.UserWithoutAuthoritiesProjection;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author Roland Krüger
 */
@RepositoryRestResource(excerptProjection = UserWithoutAuthoritiesProjection.class)
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findByUsername(@Param("username") String username);

    User findByRegistrationConfirmationToken(@Param("token") String registrationConfirmationToken);

    @RestResource(exported = false)
    @Override Iterable<User> findAll();
}
