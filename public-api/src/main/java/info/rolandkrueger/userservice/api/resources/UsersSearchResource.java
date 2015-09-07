package info.rolandkrueger.userservice.api.resources;

import info.rolandkrueger.userservice.api._internal.AbstractPagedResource;
import info.rolandkrueger.userservice.api._internal.AbstractResource;
import info.rolandkrueger.userservice.api.model.EmptyApiData;
import info.rolandkrueger.userservice.api.model.UserApiData;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;

/**
 * @author Roland Krüger
 */
public class UsersSearchResource extends AbstractResource<EmptyApiData> {

    public UsersSearchResource(Link self) {
        super(self);
    }

    @Override
    protected ParameterizedTypeReference<EmptyApiData> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<EmptyApiData>() {
        };
    }

    @Override
    protected Class<EmptyApiData> getResourceType() {
        return EmptyApiData.class;
    }

    public final UsersSearchResultResource findByUsername(String username) {
        return new UsersSearchResultResource(getFindByUsernameLink().expand(username));
    }

    public final UsersSearchResultResource findByRegistrationConfirmationToken(String registrationConfirmationToken) {
        return new UsersSearchResultResource(getFindByRegistrationConfirmationTokenLink().expand(registrationConfirmationToken));
    }

    private Link getFindByUsernameLink() {
        return getLinkFor(getResponseEntity(), "findByUsername");
    }

    private Link getFindByRegistrationConfirmationTokenLink() {
        return getLinkFor(getResponseEntity(), "findByRegistrationConfirmationToken");
    }

    public class UsersSearchResultResource extends AbstractPagedResource<UserApiData, UsersSearchResultResource> {

        private UsersSearchResultResource(Link self) {
            super(self, self);
        }

        @Override
        protected ParameterizedTypeReference<UserApiData> getParameterizedTypeReference() {
            return new ParameterizedTypeReference<UserApiData>() {
            };
        }

        @Override
        protected Class<UserApiData> getResourceType() {
            return UserApiData.class;
        }

        @Override
        protected UsersSearchResultResource createResourceInstance(Link self) {
            return new UsersSearchResultResource(self);
        }

        @Override
        protected ParameterizedTypeReference<PagedResources<UserApiData>> getParameterizedTypeReferencePaged() {
            return new ParameterizedTypeReference<PagedResources<UserApiData>>() {
            };
        }
    }
}
