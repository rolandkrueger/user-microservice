package info.rolandkrueger.userservice.api;

import info.rolandkrueger.userservice.api._internal.AbstractPagedResource;
import info.rolandkrueger.userservice.api.model.UserApiData;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;

/**
 * @author Roland Krüger
 */
public class UserResource extends AbstractPagedResource<UserApiData, UserResource> {

    protected UserResource(Link self) {
        this(self, self);
    }

    protected UserResource(Link templatedBaseLink, Link self) {
        super(templatedBaseLink, self);
    }

    @Override
    protected UserResource createResourceInstance(Link self) {
        return new UserResource(self);
    }

    @Override
    protected Class<UserApiData> getResourceType() {
        return UserApiData.class;
    }

    @Override
    protected ParameterizedTypeReference<PagedResources<UserApiData>> getParameterizedTypeReferencePaged() {
        return new ParameterizedTypeReference<PagedResources<UserApiData>>() {
        };
    }

    @Override
    protected ParameterizedTypeReference<UserApiData> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<UserApiData>() {
        };
    }

    private String getSortByProperty(UsersSort sortBy) {
        return sortBy == null ? null : sortBy.getProperty();
    }
}
