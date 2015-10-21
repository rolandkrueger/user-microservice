package info.rolandkrueger.userservice.api.model;

import info.rolandkrueger.userservice.api._internal.model.AbstractBaseApiData;
import info.rolandkrueger.userservice.api.resources.EmptyResource;
import org.springframework.hateoas.Link;

/**
 * Empty API data class with no data fields. Is used by {@link EmptyResource}s.
 *
 * @author Roland Krüger
 * @see EmptyResource
 */
public class EmptyApiData extends AbstractBaseApiData<EmptyResource> {
    @Override
    protected EmptyResource createNewResource(Link self) {
        return new EmptyResource(self);
    }
}
