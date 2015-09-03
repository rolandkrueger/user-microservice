package info.rolandkrueger.userservice.api.resources;

import info.rolandkrueger.userservice.api._internal.AbstractPagedResource;
import info.rolandkrueger.userservice.api._internal.RestApiConstants;
import info.rolandkrueger.userservice.api.enums.UserProjections;
import info.rolandkrueger.userservice.api.model.UserApiData;
import info.rolandkrueger.userservice.api.enums.SortDirection;
import info.rolandkrueger.userservice.api.enums.UsersSort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;

import java.util.Collections;

/**
 * @author Roland Krüger
 */
public class UsersResource extends AbstractPagedResource<UserApiData, UsersResource> {

    protected UsersResource(Link self) {
        this(self, self);
    }

    protected UsersResource(Link templatedBaseLink, Link self) {
        super(templatedBaseLink, self);
    }

    public UsersResource sort(UsersSort sortBy, SortDirection direction) {
        return sort(getSortByProperty(sortBy), direction);
    }

    public UsersResource useProjection(UserProjections projection) {
        return new UsersResource(self.expand(Collections.singletonMap(RestApiConstants.PROJECTION, projection.getName())));
    }

    public UsersResource goToPageSorted(Integer page, Integer size, UsersSort sortBy, SortDirection
            direction) {
        return goToPageSorted(page, size, getSortByProperty(sortBy), direction);
    }

    @Override
    protected UsersResource createResourceInstance(Link self) {
        return new UsersResource(self);
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
