package info.rolandkrueger.userservice.api.resources;

import info.rolandkrueger.userservice.api._internal.AbstractResource;
import info.rolandkrueger.userservice.api.model.EmptyApiData;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;

/**
 * @author Roland Krüger
 */
public class ConfirmRegistrationResource extends AbstractResource<EmptyApiData> {

    public ConfirmRegistrationResource(Link self) {
        super(self);
    }

    public final ResponseEntity confirmRegistration() {
        return restTemplate.postForEntity(self.getHref(), null, ResponseEntity.class);
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
}
