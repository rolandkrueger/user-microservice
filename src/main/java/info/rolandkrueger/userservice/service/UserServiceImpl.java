package info.rolandkrueger.userservice.service;

import info.rolandkrueger.userservice.model.User;
import info.rolandkrueger.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Roland Krüger
 */
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByRegistrationConfirmationToken(String confirmationToken) {
        return userRepository.findByRegistrationConfirmationToken(confirmationToken);
    }
}
