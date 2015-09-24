package info.rolandkrueger.userservice.controller;

import com.google.common.base.Strings;
import info.rolandkrueger.userservice.api._internal.RestApiConstants;
import info.rolandkrueger.userservice.api.model.UserRegistrationApiData;
import info.rolandkrueger.userservice.model.User;
import info.rolandkrueger.userservice.model.UserRegistrationResource;
import info.rolandkrueger.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * @author Roland Krüger
 */
@RestController
@ExposesResourceFor(UserRegistrationResource.class)
@RequestMapping("/" + RestApiConstants.REGISTRATIONS_RESOURCE)
public class UserRegistrationRestController implements ResourceProcessor<RepositoryLinksResource> {

    @Autowired
    private UserService userService;

    @RequestMapping(RestApiConstants.SEARCH_RESOURCE)
    public ResponseEntity<UserRegistrationResource> searchRegistration(@RequestParam String token) {
        User user = userService.findByRegistrationConfirmationToken(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new UserRegistrationResource(user), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public RegistrationsResource getResource() {
        return new RegistrationsResource();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserRegistrationResource> registerUser(@RequestBody UserRegistrationApiData
                                                                             userRegistration) {
        if (Strings.isNullOrEmpty(userRegistration.getUsername())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (userService.findUserByUsername(userRegistration.getUsername()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        User newUser = new User(userRegistration.getUsername());
        newUser.setEmail(userRegistration.getEmail());
        newUser.setUnencryptedPassword(userRegistration.getPassword());
        newUser.createRegistrationConfirmationToken();
        newUser.setEnabled(false);
        userService.save(newUser);

        userRegistration.setRegistrationConfirmationToken(newUser.getRegistrationConfirmationToken());
        return new ResponseEntity<>(new UserRegistrationResource(userRegistration), HttpStatus.CREATED);
    }

    @RequestMapping(value = "{token}/confirm", method = RequestMethod.POST)
    public ResponseEntity confirmRegistration(@PathVariable("token") String registrationConfirmationToken) {
        User user = userService.findByRegistrationConfirmationToken(registrationConfirmationToken);
        if (user == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        user.clearRegistrationConfirmationToken();
        user.setEnabled(true);

        userService.save(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public RepositoryLinksResource process(RepositoryLinksResource resource) {
        if (resource != null) {
            resource.add(linkTo(UserRegistrationRestController.class).withRel(RestApiConstants
                    .REGISTRATIONS_RESOURCE));
        }
        return resource;
    }

    public static class RegistrationsResource extends ResourceSupport {
        public RegistrationsResource() {
            add(linkTo(UserRegistrationRestController.class).withSelfRel());
            Link searchLink = linkTo(UserRegistrationRestController.class).slash(RestApiConstants.SEARCH_RESOURCE)
                    .withRel(RestApiConstants.SEARCH_RESOURCE);
            add(new Link(new UriTemplate(searchLink.getHref())
                    .with(RestApiConstants.TOKEN_PARAM, TemplateVariable.VariableType.REQUEST_PARAM),
                    searchLink.getRel()));
        }
    }
}
