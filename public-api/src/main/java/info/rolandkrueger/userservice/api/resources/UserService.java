package info.rolandkrueger.userservice.api.resources;

import com.google.common.base.Preconditions;
import info.rolandkrueger.userservice.api._internal.AbstractResource;
import info.rolandkrueger.userservice.api._internal.AbstractRestClient;
import info.rolandkrueger.userservice.api._internal.RestApiConstants;
import info.rolandkrueger.userservice.api._internal.model.BaseApiData;
import info.rolandkrueger.userservice.api.exceptions.UnexpectedAPIFormatException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

/**
 * @author Roland Krüger
 */
public class UserService extends AbstractResource<BaseApiData> {

    private Link authoritiesLink;
    private Link usersLink;

    public UserService(Link self) {
        super(self);
    }

    @Override
    protected Class<BaseApiData> getResourceType() {
        return BaseApiData.class;
    }

    @Override
    protected ParameterizedTypeReference<BaseApiData> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<BaseApiData>() {
        };
    }

    public void init() {
        ResponseEntity<BaseApiData> responseEntity = getResponseEntity();
        authoritiesLink = getLinkFor(responseEntity, RestApiConstants.AUTHORITIES_RESOURCE);
        usersLink = getLinkFor(responseEntity, RestApiConstants.USERS_RESOURCE);
    }

    public AuthoritiesResource authorities(Integer page, Integer size) {
        return authorities().goToPage(page, size);
    }

    public AuthoritiesResource authorities() {
        return new AuthoritiesResource(authoritiesLink);
    }

    public UsersResource users() {
        return new UsersResource(usersLink);
    }

    public UsersResource users(Integer page, Integer size) {
        return users().goToPage(page, size);
    }
}
