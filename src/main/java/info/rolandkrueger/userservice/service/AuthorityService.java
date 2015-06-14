package info.rolandkrueger.userservice.service;

import java.util.List;

import info.rolandkrueger.userservice.model.Authority;
import org.springframework.data.domain.Sort;

/**
 * @author Roland Krüger
 */
public interface AuthorityService {

    Authority findByAuthority(String name);

    List<Authority> getAuthorityList(int page, int size, Sort.Direction sort);

    Authority create(Authority authority);

    Authority update(Authority authority);

    void delete(Authority authority);
}
