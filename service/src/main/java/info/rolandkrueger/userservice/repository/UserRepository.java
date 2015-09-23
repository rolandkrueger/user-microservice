package info.rolandkrueger.userservice.repository;

import info.rolandkrueger.userservice.api._internal.RestApiConstants;
import info.rolandkrueger.userservice.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Roland Krüger
 */
@RepositoryRestResource
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByUsername(@Param(RestApiConstants.USERNAME_PARAM) String username);

    @RestResource(exported = false)
    User findByRegistrationConfirmationToken(String registrationConfirmationToken);

    @RestResource(exported = false)
    @Override Iterable<User> findAll();

    @RestResource(exported = false)
    @Override <S extends User> S save(S entity);

    @RestResource(exported = false)
    @Override <S extends User> Iterable<S> save(Iterable<S> entities);
}
