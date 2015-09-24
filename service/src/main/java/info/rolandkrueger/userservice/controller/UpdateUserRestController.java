package info.rolandkrueger.userservice.controller;

import com.google.common.base.Preconditions;
import info.rolandkrueger.userservice.api._internal.RestApiConstants;
import info.rolandkrueger.userservice.api.model.UserApiData;
import info.rolandkrueger.userservice.model.User;
import info.rolandkrueger.userservice.service.AuthorityService;
import info.rolandkrueger.userservice.service.UserService;
import info.rolandkrueger.userservice.service.exception.UserNotFoundException;
import info.rolandkrueger.userservice.service.exception.UsernameAlreadyInUseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author Roland Krüger
 */
@RestController
@ExposesResourceFor(User.class)
@RequestMapping("/" + RestApiConstants.UPDATE_USER_RESOURCE)
public class UpdateUserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    AuthorityService authorityService;

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody final UserApiData user) {
        Preconditions.checkNotNull(user);
        final User userFromDB = loadUpdatedUserFromDatabase(id);

        if (usernameUpdateRequested(user, userFromDB)) {
            if (user.getUsername() != null && usernameIsAvailable(user)) {
                userFromDB.setUsername(user.getUsername());
            } else {
                throw new UsernameAlreadyInUseException();
            }
        }

        userFromDB.setAccountNonExpired(user.isAccountNonExpired());
        userFromDB.setAccountNonLocked(user.isAccountNonLocked());
        userFromDB.setCredentialsNonExpired(user.isCredentialsNonExpired());
        userFromDB.setEmail(user.getEmail());
        userFromDB.setEnabled(user.isEnabled());
        userFromDB.setRememberMeToken(user.getRememberMeToken());

        userFromDB.getAuthorities().clear();
        userFromDB.getAuthorities().addAll(
                user.getAuthorities()
                        .stream()
                        .map(authority -> authorityService.findByAuthority(authority.getAuthority()))
                        .filter(authority -> authority != null)
                        .distinct()
                        .collect(Collectors.toList()));

        if (!isNullOrEmpty(firstNonNull(user.getPassword(), ""))) {
            userFromDB.setUnencryptedPassword(user.getPassword());
        }

        userFromDB.setLastModified(LocalDateTime.now());

        return new ResponseEntity<>(userService.save(userFromDB), OK);
    }

    private User loadUpdatedUserFromDatabase(Long userId) {
        final User userFromDB = userService.findById(userId);
        if (userFromDB == null) {
            throw new UserNotFoundException("Cannot update user with ID " + userId + ". User does not exist.");
        }
        return userFromDB;
    }

    private boolean usernameIsAvailable(UserApiData user) {
        return userService.findUserByUsername(user.getUsername()) == null;
    }

    private boolean usernameUpdateRequested(UserApiData user, User userFromDB) {
        return !userFromDB.getUsername().equals(user.getUsername());
    }
}
