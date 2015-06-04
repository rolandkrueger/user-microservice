package info.rolandkrueger.userservice.repository;

import info.rolandkrueger.userservice.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Roland Krüger
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findByUsername(String username);

    User findByRegistrationConfirmationToken(String registrationConfirmationToken);
}
