package info.rolandkrueger.userservice.service;

import java.util.List;

import info.rolandkrueger.userservice.model.User;
import info.rolandkrueger.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author Roland Krüger
 */
@Service
public class UserServiceImpl implements UserService {
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

    @Override
    public List<User> getUserList(int page, int size,  Sort.Direction sort) {
        return userRepository.findAll(new PageRequest(page, size, sort, "username")).getContent();
    }
}
