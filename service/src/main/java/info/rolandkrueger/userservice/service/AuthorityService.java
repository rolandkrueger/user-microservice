package info.rolandkrueger.userservice.service;

import info.rolandkrueger.userservice.model.Authority;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @author Roland Krüger
 */
public interface AuthorityService {

    Authority findByAuthority(String authority);

    List<Authority> getAuthorityList(int page, int size, Sort.Direction sort);

    Authority create(Authority authority);

    Authority update(Authority authority);

    void delete(Long authorityId);
}
