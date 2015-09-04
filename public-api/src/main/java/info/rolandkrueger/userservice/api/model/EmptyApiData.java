package info.rolandkrueger.userservice.api.model;

import info.rolandkrueger.userservice.api._internal.model.AbstractBaseApiData;
import info.rolandkrueger.userservice.api.resources.EmptyResource;
import org.springframework.hateoas.Link;

/**
 * @author Roland Krüger
 */
public class EmptyApiData extends AbstractBaseApiData<EmptyResource> {
    @Override
    protected EmptyResource createNewResource(Link self) {
        return new EmptyResource(self);
    }
}
