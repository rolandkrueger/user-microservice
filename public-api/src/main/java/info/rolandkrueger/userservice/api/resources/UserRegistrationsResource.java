package info.rolandkrueger.userservice.api.resources;

import info.rolandkrueger.userservice.api._internal.AbstractResource;
import info.rolandkrueger.userservice.api._internal.RestApiConstants;
import info.rolandkrueger.userservice.api.model.UserRegistrationApiData;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

/**
 * @author Roland Krüger
 */
public class UserRegistrationsResource extends AbstractResource<UserRegistrationApiData> implements
        CanCreate<UserRegistrationApiData> {

    public UserRegistrationsResource(Link self) {
        super(self);
    }

    @Override
    public ResponseEntity<UserRegistrationApiData> create(UserRegistrationApiData entity) throws RestClientException {
        return super.createInternal(entity);
    }

    public final UserRegistrationSearchResultResource findByToken(String token) {
        return new UserRegistrationSearchResultResource(getLinkFor(getResponseEntity(), RestApiConstants
                .SEARCH_RESOURCE).expand(token));
    }

    @Override
    protected ParameterizedTypeReference<UserRegistrationApiData> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<UserRegistrationApiData>() {
        };
    }

    @Override
    protected Class<UserRegistrationApiData> getResourceType() {
        return UserRegistrationApiData.class;
    }

    public class UserRegistrationSearchResultResource extends AbstractResource<UserRegistrationApiData> {

        public UserRegistrationSearchResultResource(Link self) {
            super(self);
        }

        public final boolean exists() {
            try {
                read();
            } catch (HttpStatusCodeException exc) {
                return false;
            }
            return true;
        }

        public final ResponseEntity confirmRegistration() {
            return restTemplate.postForEntity(getLinkFor(getResponseEntity(), RestApiConstants.CONFIRM).getHref(), null, ResponseEntity.class);
        }

        @Override
        protected ParameterizedTypeReference<UserRegistrationApiData> getParameterizedTypeReference() {
            return new ParameterizedTypeReference<UserRegistrationApiData>() {
            };
        }

        @Override
        protected Class<UserRegistrationApiData> getResourceType() {
            return UserRegistrationApiData.class;
        }
    }
}
