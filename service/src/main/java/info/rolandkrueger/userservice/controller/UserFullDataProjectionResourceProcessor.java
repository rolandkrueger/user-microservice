package info.rolandkrueger.userservice.controller;

import info.rolandkrueger.userservice.api._internal.RestApiConstants;
import info.rolandkrueger.userservice.model.User;
import info.rolandkrueger.userservice.model.UserFullDataProjection;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

/**
 * @author Roland Krüger
 */
@Component
public class UserFullDataProjectionResourceProcessor  implements ResourceProcessor<Resource<UserFullDataProjection>> {
    @Override
    public Resource<UserFullDataProjection> process(Resource<UserFullDataProjection> resource) {
        resource.add(ControllerLinkBuilder
                .linkTo(UpdateUserRestController.class)
                .slash(resource.getContent())
                .withRel(RestApiConstants.UPDATE));
        return resource;
    }
}
