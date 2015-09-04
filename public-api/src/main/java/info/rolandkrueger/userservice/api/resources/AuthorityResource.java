package info.rolandkrueger.userservice.api.resources;

import info.rolandkrueger.userservice.api._internal.AbstractResource;
import info.rolandkrueger.userservice.api.model.AuthorityApiData;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.web.client.RestClientException;

/**
 * @author Roland Krüger
 */
public class AuthorityResource extends AbstractResource<AuthorityApiData> implements CanUpdateResource,
        CanDeleteResource {

    public AuthorityResource(Link self, AuthorityApiData data) {
        super(self, data);
    }

    @Override
    protected ParameterizedTypeReference<AuthorityApiData> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<AuthorityApiData>() {
        };
    }

    @Override
    protected Class<AuthorityApiData> getResourceType() {
        return AuthorityApiData.class;
    }

    @Override
    public void delete() throws RestClientException {
        super.deleteInternal(getApiData());
    }

    @Override
    public void update() throws RestClientException {
        super.updateInternal(getApiData());
    }
}
