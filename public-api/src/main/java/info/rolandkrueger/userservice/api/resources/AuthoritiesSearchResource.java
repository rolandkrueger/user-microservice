package info.rolandkrueger.userservice.api.resources;

import info.rolandkrueger.userservice.api._internal.AbstractPagedResource;
import info.rolandkrueger.userservice.api._internal.AbstractResource;
import info.rolandkrueger.userservice.api.model.AuthorityApiData;
import info.rolandkrueger.userservice.api.model.EmptyApiData;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;

import java.util.Optional;

/**
 * Authority search resource. Provides methods to search for authority objects.
 *
 * @author Roland Krüger
 */
public class AuthoritiesSearchResource extends AbstractResource<EmptyApiData> {

    AuthoritiesSearchResource(Link self) {
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

    /**
     * Searches for a given authority name. If no such object exists an empty result is returned.
     *
     * @param authority authority name to search for
     * @return a resource object that contains the search result
     */
    public AuthoritiesSearchResultResource findByAuthority(String authority) {
        return new AuthoritiesSearchResultResource(getFindByAuthorityLink().expand(authority));
    }

    /**
     * Searches for a given authority name and returns an {@link Optional} containing the requested authority object
     * if available.
     *
     * @param authority authority name to search for
     * @return Optional containing the requested authority object if that could be found
     */
    public Optional<AuthorityApiData> findOneByAuthority(String authority) {
        return findByAuthority(authority).getData().stream().findFirst();
    }

    private Link getFindByAuthorityLink() {
        return getLinkFor(getResponseEntity(), "findByAuthority");
    }

    public class AuthoritiesSearchResultResource extends AbstractPagedResource<AuthorityApiData, AuthoritiesSearchResultResource> {

        protected AuthoritiesSearchResultResource(Link self) {
            super(self, self);
        }

        @Override
        protected AuthoritiesSearchResultResource createResourceInstance(Link self) {
            return new AuthoritiesSearchResultResource(self);
        }

        @Override
        protected ParameterizedTypeReference<PagedResources<AuthorityApiData>> getParameterizedTypeReferencePaged() {
            return new ParameterizedTypeReference<PagedResources<AuthorityApiData>>() {
            };
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
    }
}
