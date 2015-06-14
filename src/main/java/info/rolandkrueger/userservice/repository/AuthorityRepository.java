package info.rolandkrueger.userservice.repository;

import info.rolandkrueger.userservice.model.Authority;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AuthorityRepository extends PagingAndSortingRepository<Authority, Long> {
    Authority findByAuthority(String authority);
}
