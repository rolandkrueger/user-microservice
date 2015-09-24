package info.rolandkrueger.userservice.repository;

import info.rolandkrueger.userservice.api._internal.RestApiConstants;
import info.rolandkrueger.userservice.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * @author Roland Krüger
 */
@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(@Param(RestApiConstants.USERNAME_PARAM) String username);

    @Query("select u from User u where u.username = :username and u.enabled = true " +
            "and u.accountNonLocked = true " +
            "and u.accountNonExpired = true " +
            "and u.credentialsNonExpired = true")
    User findUserForLogin(@Param(RestApiConstants.USERNAME_PARAM) String username);

    @RestResource(exported = false)
    User findByRegistrationConfirmationToken(String registrationConfirmationToken);

    @RestResource(exported = false)
    @Override
    <S extends User> S save(S entity);

    @Override
    @RestResource(exported = false)
    List<User> findAll();

    @Override
    @RestResource(exported = false)
    List<User> findAll(Iterable<Long> iterable);

    @Override
    @RestResource(exported = false)
    List<User> findAll(Sort sort);

    @Override
    @RestResource(exported = false)
    <S extends User> List<S> save(Iterable<S> iterable);

    @Override
    <S extends User> S saveAndFlush(S s);
}
