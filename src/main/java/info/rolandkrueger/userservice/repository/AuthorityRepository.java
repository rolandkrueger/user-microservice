package info.rolandkrueger.userservice.repository;

import info.rolandkrueger.userservice.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByAuthority(String authority);
}
