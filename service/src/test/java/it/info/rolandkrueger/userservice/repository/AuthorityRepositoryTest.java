package it.info.rolandkrueger.userservice.repository;

import info.rolandkrueger.userservice.UserMicroserviceApplication;
import info.rolandkrueger.userservice.model.Authority;
import info.rolandkrueger.userservice.repository.AuthorityRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Roland Krüger
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UserMicroserviceApplication.class)
@Transactional
public class AuthorityRepositoryTest {
    private Authority authority;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Before
    public void setUp() {
        authority = new Authority("editor");
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void test_authority_unique_in_db() {
        authorityRepository.save(authority);
        authorityRepository.save(new Authority("editor"));
    }

    @Test
    public void testFindByAuthority() {
        Authority foundAuthority = authorityRepository.findByAuthority("user");
        assertThat(foundAuthority, is(notNullValue()));

        foundAuthority = authorityRepository.findByAuthority("invalid");
        assertThat(foundAuthority, is(nullValue()));
    }
}
