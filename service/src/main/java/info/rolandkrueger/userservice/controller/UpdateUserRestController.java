package info.rolandkrueger.userservice.controller;

import com.google.common.base.Preconditions;
import info.rolandkrueger.userservice.api._internal.RestApiConstants;
import info.rolandkrueger.userservice.api.model.UserApiData;
import info.rolandkrueger.userservice.model.Authority;
import info.rolandkrueger.userservice.model.User;
import info.rolandkrueger.userservice.repository.AuthorityRepository;
import info.rolandkrueger.userservice.repository.UserRepository;
import info.rolandkrueger.userservice.service.exception.UserNotFoundException;
import info.rolandkrueger.userservice.service.exception.UsernameAlreadyInUseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author Roland Krüger
 */
@RestController
@ExposesResourceFor(User.class)
@RequestMapping("/" + RestApiConstants.UPDATE_USER_RESOURCE)
public class UpdateUserRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

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
                        .map(authority -> authorityRepository.findByAuthority(authority.getAuthority()))
                        .filter(authority -> authority != null)
                        .distinct()
                        .collect(Collectors.toList()));

        if (!isNullOrEmpty(firstNonNull(user.getPassword(), ""))) {
            userFromDB.setUnencryptedPassword(user.getPassword());
        }

        userFromDB.setLastModified(LocalDateTime.now());

        return new ResponseEntity<>(userRepository.save(userFromDB), OK);
    }

    private User loadUpdatedUserFromDatabase(Long userId) {
        final User userFromDB = userRepository.findOne(userId);
        if (userFromDB == null) {
            throw new UserNotFoundException("Cannot update user with ID " + userId + ". User does not exist.");
        }
        return userFromDB;
    }

    private boolean usernameIsAvailable(UserApiData user) {
        return userRepository.findByUsername(user.getUsername()) == null;
    }

    private boolean usernameUpdateRequested(UserApiData user, User userFromDB) {
        return !userFromDB.getUsername().equals(user.getUsername());
    }
}
