package info.rolandkrueger.userservice.service;

import info.rolandkrueger.userservice.UserMicroserviceApplication;
import info.rolandkrueger.userservice.model.Authority;
import it.info.rolandkrueger.userservice.testsupport.AbstractServiceTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static info.rolandkrueger.userservice.application.DevelopmentProfileConfiguration.*;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.number.OrderingComparison.greaterThan;

/**
 * @author Roland Krüger
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UserMicroserviceApplication.class)
@Transactional
public class AuthorityServiceTest extends AbstractServiceTest {

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() {
        createTestData(authorityService, userService);
    }

    @Test
    public void testFindByAuthority() throws Exception {
        final Authority authority = authorityService.findByAuthority("admin");
        assertThat(authority, is(notNullValue()));
        assertThat(authority.getDescription(), is(adminAuthority.getDescription()));
    }

    @Test
    public void testFindByAuthority_NotFound() throws Exception {
        final Authority authority = authorityService.findByAuthority("invalid");
        assertThat(authority, is(nullValue()));
    }

    @Test
    public void testGetAuthorityList() throws Exception {
        List<Authority> authorityList = authorityService.getAuthorityList(0, 3, Sort.Direction.ASC);
        assertThat(authorityList, contains(adminAuthority, developerAuthority, userAuthority));

        authorityList = authorityService.getAuthorityList(0, 3, Sort.Direction.DESC);
        assertThat(authorityList, contains(userAuthority, developerAuthority, adminAuthority));
    }

    @Test
    public void testCreate() throws Exception {
        Authority authority = new Authority("test", "test");
        final Authority created = authorityService.create(authority);
        assertThat(created, is(authority));
        assertThat(created.getId(), is(greaterThan(0L)));

        final Authority readAuthority = authorityService.findByAuthority("test");
        assertThat(readAuthority, is(created));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testCreate_FailsWithDuplicate() throws Exception {
        Authority authority = new Authority(adminAuthority.getAuthority());
        authorityService.create(authority);
    }

    @Test
    public void testUpdate() throws Exception {
        final Authority admin = authorityService.findByAuthority(adminAuthority.getAuthority());
        admin.setDescription("test");
        final Authority updated = authorityService.update(admin);
        assertThat(updated.getId(), is(adminAuthority.getId()));
        assertThat(updated.getDescription(), is("test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_FailsWithUnknown() throws Exception {
        Authority unknown = new Authority("unknown");
        unknown.setId(17L);
        authorityService.update(unknown);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_FailsWithNewAuthorityObject() throws Exception {
        Authority unknown = new Authority("unknown");
        authorityService.update(unknown);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testDelete_WithForeignKeyConstraintViolation() throws Exception {
        authorityService.delete(adminAuthority.getId());
        assertThat(authorityService.findByAuthority(adminAuthority.getAuthority()), is(nullValue()));
    }

    @Test
    public void testDelete() {
        Authority authority = new Authority("test");
        final Authority created = authorityService.create(authority);
        authorityService.delete(created.getId());
        assertThat(authorityService.findByAuthority("test"), is(nullValue()));
    }
}