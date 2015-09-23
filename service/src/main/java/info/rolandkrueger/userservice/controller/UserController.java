package info.rolandkrueger.userservice.controller;

import com.google.common.base.Preconditions;
import info.rolandkrueger.userservice.api._internal.RestApiConstants;
import info.rolandkrueger.userservice.model.User;
import info.rolandkrueger.userservice.repository.UserRepository;
import info.rolandkrueger.userservice.service.exception.UserNotFoundException;
import info.rolandkrueger.userservice.service.exception.UsernameAlreadyInUseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author Roland Krüger
 */
@RestController
@RequestMapping("/" + RestApiConstants.USERS_RESOURCE + "/update")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<User> updateUser(final User user) {
        Preconditions.checkNotNull(user);
        final User userFromDB = loadUpdatedUserFromDatabase(user);

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
        userFromDB.getAuthorities().addAll(firstNonNull(user.getAuthorities(), emptyList()));

        if (isNullOrEmpty(firstNonNull(user.getPassword(), "").trim())) {
            userFromDB.setUnencryptedPassword(user.getPassword());
        }

        userFromDB.setLastModified(LocalDateTime.now());

        return new ResponseEntity<>(userRepository.save(userFromDB), OK);
    }

    private User loadUpdatedUserFromDatabase(User user) {
        final User userFromDB = userRepository.findOne(user.getId());
        if (userFromDB == null) {
            throw new UserNotFoundException("Cannot update user " + user + ". User does not exist.");
        }
        return userFromDB;
    }

    private boolean usernameIsAvailable(User user) {
        return userRepository.findByUsername(user.getUsername()) == null;
    }

    private boolean usernameUpdateRequested(User user, User userFromDB) {
        return !userFromDB.getUsername().equals(user.getUsername());
    }
}
