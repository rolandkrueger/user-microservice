package info.rolandkrueger.userservice.controller;

import info.rolandkrueger.userservice.api._internal.RestApiConstants;
import info.rolandkrueger.userservice.model.UserExcerptProjection;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

/**
 * @author Roland Krüger
 */
@Component
public class UserExcerptProjectionResourceProcessor implements ResourceProcessor<Resource<UserExcerptProjection>> {
    @Override
    public Resource<UserExcerptProjection> process(Resource<UserExcerptProjection> resource) {
        resource.add(ControllerLinkBuilder
                .linkTo(UpdateUserRestController.class)
                .slash(resource.getContent())
                .withRel(RestApiConstants.UPDATE));
        return resource;
    }
}
