package info.rolandkrueger.userservice.controller;

import info.rolandkrueger.userservice.api._internal.RestApiConstants;
import info.rolandkrueger.userservice.model.User;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

/**
 * @author Roland Krüger
 */
@Component
public class UserResourceProcessor implements ResourceProcessor<Resource<User>> {
    @Override
    public Resource<User> process(Resource<User> resource) {
        resource.add(ControllerLinkBuilder
                .linkTo(UpdateUserRestController.class)
                .slash(resource.getContent())
                .withRel(RestApiConstants.UPDATE));
        return resource;
    }
}
